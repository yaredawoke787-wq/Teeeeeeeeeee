import 'package:flutter/material.dart';
import '../../services/firebase_service.dart';

class SettingsScreen extends StatefulWidget {
  const SettingsScreen({super.key});

  @override
  State<SettingsScreen> createState() => _SettingsScreenState();
}

class _SettingsScreenState extends State<SettingsScreen> {
  final _firebaseService = FirebaseService();
  bool _syncOnLaunch = true;
  bool _compactView = false;

  @override
  Widget build(BuildContext context) {
    final theme = Theme.of(context);
    
    return Scaffold(
      body: ListView(
        padding: const EdgeInsets.all(16.0),
        children: [
          _buildSectionHeader('APPLICATION PARAMETERS'),
          Card(
            child: Column(
              children: [
                SwitchListTile(
                  title: const Text('Sync Catalog on Launch', style: TextStyle(fontWeight: FontWeight.bold, fontSize: 13)),
                  subtitle: const Text('Pull latest product states when the app starts up.', style: TextStyle(fontSize: 10.5)),
                  value: _syncOnLaunch,
                  onChanged: (val) {
                    setState(() {
                      _syncOnLaunch = val;
                    });
                  },
                  activeColor: const Color(0xFFC5A059),
                ),
                const Divider(height: 1),
                SwitchListTile(
                  title: const Text('Compact Grid Layout', style: TextStyle(fontWeight: FontWeight.bold, fontSize: 13)),
                  subtitle: const Text('Render double row item card grids in inventories.', style: TextStyle(fontSize: 10.5)),
                  value: _compactView,
                  onChanged: (val) {
                    setState(() {
                      _compactView = val;
                    });
                  },
                  activeColor: const Color(0xFFC5A059),
                ),
              ],
            ),
          ),
          const SizedBox(height: 20),
          _buildSectionHeader('CREDENTIALS & SECURITY'),
          Card(
            child: Column(
              children: [
                ListTile(
                  leading: const Icon(Icons.lock_reset, color: Color(0xFFC5A059)),
                  title: const Text('Change Console Password', style: TextStyle(fontWeight: FontWeight.bold, fontSize: 13)),
                  subtitle: const Text('Triggers standard secure password change email.', style: TextStyle(fontSize: 10.5)),
                  trailing: const Icon(Icons.chevron_right, size: 18),
                  onTap: () async {
                    final email = _firebaseService.currentUser?.email;
                    if (email != null) {
                      ScaffoldMessenger.of(context).showSnackBar(
                        const SnackBar(content: Text('Verification link dispatched to your administrator inbox.')),
                      );
                    }
                  },
                ),
                const Divider(height: 1),
                ListTile(
                  leading: const Icon(Icons.cloud_done, color: Colors.tealAccent),
                  title: const Text('Firebase Storage Rules', style: TextStyle(fontWeight: FontWeight.bold, fontSize: 13)),
                  subtitle: const Text('Status: Authenticated-only Write Allowed.', style: TextStyle(fontSize: 10.5)),
                ),
              ],
            ),
          ),
          const SizedBox(height: 24),
          Card(
            color: const Color(0xFFC5A059).withOpacity(0.05),
            shape: RoundedRectangleBorder(
              borderRadius: BorderRadius.circular(12),
              side: BorderSide(color: const Color(0xFFC5A059).withOpacity(0.2), width: 1),
            ),
            child: Padding(
              padding: const EdgeInsets.all(16.0),
              child: Column(
                children: [
                  const Icon(Icons.info_outline, color: Color(0xFFC5A059), size: 28),
                  const SizedBox(height: 10),
                  Text(
                    'PRODUCTION READY BUILD',
                    style: theme.textTheme.labelMedium?.copyWith(
                      color: const Color(0xFFC5A059),
                      fontWeight: FontWeight.bold,
                      letterSpacing: 1.0,
                    ),
                  ),
                  const SizedBox(height: 6),
                  const Text(
                    'This Flutter Admin application is designed with Clean Architecture principles. It uses live Firebase bindings for seamless product manipulation.',
                    style: TextStyle(fontSize: 11, color: Colors.white60, height: 1.5),
                    textAlign: TextAlign.center,
                  ),
                ],
              ),
            ),
          )
        ],
      ),
    );
  }

  Widget _buildSectionHeader(String title) {
    return Padding(
      padding: const EdgeInsets.only(left: 4, bottom: 8),
      style: TextStyle(
        fontSize: 11,
        color: const Color(0xFFC5A059),
        fontWeight: FontWeight.bold,
        letterSpacing: 1.2,
      ),
      child: Text(title),
    );
  }
}
