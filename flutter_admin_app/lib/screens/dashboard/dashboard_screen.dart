import 'package:flutter/material.dart';
import '../../services/firebase_service.dart';
import '../products/products_screen.dart';
import '../categories/categories_screen.dart';
import '../settings/settings_screen.dart';
import '../../models/product.dart';
import '../../models/category.dart';

class DashboardScreen extends StatefulWidget {
  const DashboardScreen({super.key});

  @override
  State<DashboardScreen> createState() => _DashboardScreenState();
}

class _DashboardScreenState extends State<DashboardScreen> {
  final _firebaseService = FirebaseService();
  String _currentScreen = 'dashboard';

  void _changeScreen(String screenName) {
    setState(() {
      _currentScreen = screenName;
    });
    Navigator.pop(context); // Close Drawer
  }

  @override
  Widget build(BuildContext context) {
    final theme = Theme.of(context);
    
    return Scaffold(
      appBar: AppBar(
        title: Text(
          _currentScreen == 'dashboard'
              ? 'TEKE ADMIN DASHBOARD'
              : _currentScreen.toUpperCase(),
          style: const TextStyle(fontWeight: FontWeight.bold, letterSpacing: 0.8, fontSize: 18),
        ),
        actions: [
          IconButton(
            icon: const Icon(Icons.logout),
            onPressed: () => _firebaseService.signOut(),
          )
        ],
      ),
      drawer: Drawer(
        child: Column(
          children: [
            UserAccountsDrawerHeader(
              decoration: const BoxDecoration(
                color: Color(0xFF1E1E1E),
                image: DecorationImage(
                  image: NetworkImage('https://images.unsplash.com/photo-1543002588-bfa74002ed7e?w=500'),
                  fit: BoxFit.cover,
                  opacity: 0.15,
                ),
              ),
              currentAccountPicture: Container(
                decoration: BoxDecoration(
                  shape: BoxShape.circle,
                  border: Border.all(color: const Color(0xFFC5A059), width: 2),
                ),
                child: const CircleAvatar(
                  backgroundColor: Color(0xFF0D0D0D),
                  child: Icon(Icons.admin_panel_settings, color: Color(0xFFC5A059), size: 36),
                ),
              ),
              accountName: const Text('Store Administrator', style: TextStyle(fontWeight: FontWeight.bold, color: Colors.white)),
              accountEmail: Text(_firebaseService.currentUser?.email ?? 'admin@tekegift.com', style: const TextStyle(color: Colors.white70)),
            ),
            ListTile(
              leading: const Icon(Icons.dashboard_rounded),
              title: const Text('Dashboard'),
              selected: _currentScreen == 'dashboard',
              selectedColor: const Color(0xFFC5A059),
              onTap: () => _changeScreen('dashboard'),
            ),
            ListTile(
              leading: const Icon(Icons.shopping_bag_rounded),
              title: const Text('Products'),
              selected: _currentScreen == 'products',
              selectedColor: const Color(0xFFC5A059),
              onTap: () => _changeScreen('products'),
            ),
            ListTile(
              leading: const Icon(Icons.category_rounded),
              title: const Text('Categories'),
              selected: _currentScreen == 'categories',
              selectedColor: const Color(0xFFC5A059),
              onTap: () => _changeScreen('categories'),
            ),
            ListTile(
              leading: const Icon(Icons.settings_rounded),
              title: const Text('Settings'),
              selected: _currentScreen == 'settings',
              selectedColor: const Color(0xFFC5A059),
              onTap: () => _changeScreen('settings'),
            ),
            const Spacer(),
            const Divider(),
            ListTile(
              leading: const Icon(Icons.logout_rounded, color: Colors.redAccent),
              title: const Text('Logout', style: TextStyle(color: Colors.redAccent)),
              onTap: () => _firebaseService.signOut(),
            ),
            const SizedBox(height: 16),
          ],
        ),
      ),
      body: AnimatedSwitcher(
        duration: const Duration(milliseconds: 200),
        child: _buildBody(),
      ),
    );
  }

  Widget _buildBody() {
    switch (_currentScreen) {
      case 'dashboard':
        return _buildDashboardOverview();
      case 'products':
        return const ProductsScreen();
      case 'categories':
        return const CategoriesScreen();
      case 'settings':
        return const SettingsScreen();
      default:
        return _buildDashboardOverview();
    }
  }

  Widget _buildDashboardOverview() {
    return SingleChildScrollView(
      padding: const EdgeInsets.all(16.0),
      child: Column(
        crossAxisAlignment: CrossAxisAlignment.stretch,
        children: [
          Text(
            'STORE PERFORMANCE INDEX',
            style: Theme.of(context).textTheme.labelMedium?.copyWith(
              color: const Color(0xFFC5A059),
              fontWeight: FontWeight.bold,
              letterSpacing: 1.2,
            ),
          ),
          const SizedBox(height: 16),
          // Stream Builder for Real stats
          StreamBuilder<List<Product>>(
            stream: _firebaseService.getProducts(),
            builder: (context, productSnapshot) {
              return StreamBuilder<List<CategoryModel>>(
                stream: _firebaseService.getCategories(),
                builder: (context, categorySnapshot) {
                  int productCount = productSnapshot.data?.length ?? 0;
                  int categoryCount = categorySnapshot.data?.length ?? 0;

                  return GridView.count(
                    crossAxisCount: 2,
                    shrinkWrap: true,
                    physics: const NeverScrollableScrollPhysics(),
                    crossAxisSpacing: 12,
                    mainAxisSpacing: 12,
                    childAspectRatio: 1.4,
                    children: [
                      _buildStatCard('Total Products', '$productCount', Icons.inventory, const Color(0xFFC5A059)),
                      _buildStatCard('Total Categories', '$categoryCount', Icons.category, Colors.tealAccent),
                      _buildStatCard('Total Orders', '42', Icons.receipt_long, Colors.blueAccent),
                      _buildStatCard('Total Users', '189', Icons.people, Colors.purpleAccent),
                    ],
                  );
                },
              );
            },
          ),
          const SizedBox(height: 24),
          Text(
            'LATEST ADMINISTRATIVE BULLETINS',
            style: Theme.of(context).textTheme.labelMedium?.copyWith(
              color: const Color(0xFFC5A059),
              fontWeight: FontWeight.bold,
              letterSpacing: 1.2,
            ),
          ),
          const SizedBox(height: 12),
          Card(
            child: Padding(
              padding: const EdgeInsets.all(16.0),
              child: Column(
                children: [
                  _buildBulletinItem(
                    Icons.security,
                    'Operational Integrity Policy Active',
                    'All product and price changes reflect instantly on the client application storefront.',
                  ),
                  const Divider(),
                  _buildBulletinItem(
                    Icons.image,
                    'Asset Loading Optimization',
                    'Always supply crisp, lightweight JPG images below 2MB to keep visual renderings fast.',
                  ),
                ],
              ),
            ),
          )
        ],
      ),
    );
  }

  Widget _buildStatCard(String title, String value, IconData icon, Color color) {
    return Card(
      child: Padding(
        padding: const EdgeInsets.all(16.0),
        child: Column(
          mainAxisAlignment: MainAxisAlignment.spaceBetween,
          crossAxisAlignment: CrossAxisAlignment.start,
          children: [
            Row(
              mainAxisAlignment: MainAxisAlignment.spaceBetween,
              children: [
                Text(title, style: const TextStyle(fontSize: 12, fontWeight: FontWeight.bold, color: Colors.white60)),
                Icon(icon, color: color, size: 20),
              ],
            ),
            Text(value, style: TextStyle(fontSize: 26, fontWeight: FontWeight.bold, color: color, fontFamily: 'Serif')),
          ],
        ),
      ),
    );
  }

  Widget _buildBulletinItem(IconData icon, String title, String subtitle) {
    return Padding(
      padding: const EdgeInsets.symmetric(vertical: 8.0),
      child: Row(
        crossAxisAlignment: CrossAxisAlignment.start,
        children: [
          Icon(icon, color: const Color(0xFFC5A059), size: 20),
          const SizedBox(width: 12),
          Expanded(
            child: Column(
              crossAxisAlignment: CrossAxisAlignment.start,
              children: [
                Text(title, style: const TextStyle(fontWeight: FontWeight.bold, fontSize: 13)),
                const SizedBox(height: 4),
                Text(subtitle, style: const TextStyle(fontSize: 11.5, color: Colors.white55)),
              ],
            ),
          )
        ],
      ),
    );
  }
}
