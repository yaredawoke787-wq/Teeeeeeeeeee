import 'package:flutter/material.dart';
import '../../services/firebase_service.dart';
import '../../models/product.dart';
import 'add_product_screen.dart';
import 'package:cached_network_image/cached_network_image.dart';

class ProductsScreen extends StatefulWidget {
  const ProductsScreen({super.key});

  @override
  State<ProductsScreen> createState() => _ProductsScreenState();
}

class _ProductsScreenState extends State<ProductsScreen> {
  final _firebaseService = FirebaseService();
  String _searchQuery = '';
  String _statusFilter = 'all'; // all, active, inactive
  String _categoryFilter = 'all';

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      body: Column(
        children: [
          // Filter & Search bar header
          Padding(
            padding: const EdgeInsets.all(12.0),
            child: Column(
              children: [
                TextField(
                  onChanged: (value) {
                    setState(() {
                      _searchQuery = value;
                    });
                  },
                  decoration: InputDecoration(
                    hintText: 'Search products...',
                    prefixIcon: const Icon(Icons.search, color: Color(0xFFC5A059)),
                    border: OutlineInputBorder(
                      borderRadius: BorderRadius.circular(12),
                    ),
                  ),
                ),
                const SizedBox(height: 10),
                Row(
                  mainAxisAlignment: MainAxisAlignment.spaceBetween,
                  children: [
                    // Status filters row
                    Row(
                      children: [
                        _buildFilterChip('All', 'all'),
                        const SizedBox(width: 6),
                        _buildFilterChip('Active', 'active'),
                        const SizedBox(width: 6),
                        _buildFilterChip('Inactive', 'inactive'),
                      ],
                    ),
                    // Add Product mini-btn
                    ElevatedButton.icon(
                      onPressed: () {
                        Navigator.push(
                          context,
                          MaterialPageRoute(builder: (context) => const AddProductScreen()),
                        );
                      },
                      style: ElevatedButton.styleFrom(
                        backgroundColor: const Color(0xFFC5A059),
                        foregroundColor: Colors.black,
                        shape: RoundedRectangleBorder(
                          borderRadius: BorderRadius.circular(8),
                        ),
                      ),
                      icon: const Icon(Icons.add, size: 16),
                      label: const Text('ADD', style: TextStyle(fontWeight: FontWeight.bold, fontSize: 11)),
                    ),
                  ],
                ),
              ],
            ),
          ),
          // Product Stream List
          Expanded(
            child: StreamBuilder<List<Product>>(
              stream: _firebaseService.getProducts(),
              builder: (context, snapshot) {
                if (snapshot.connectionState == ConnectionState.waiting) {
                  return const Center(child: CircularProgressIndicator(color: Color(0xFFC5A059)));
                }

                if (snapshot.hasError) {
                  return Center(child: Text('Error: ${snapshot.error}', style: const TextStyle(color: Colors.red)));
                }

                final products = snapshot.data ?? [];
                
                // Filter logic
                final filtered = products.where((prod) {
                  final matchesSearch = prod.name.toLowerCase().contains(_searchQuery.toLowerCase()) ||
                      prod.category.toLowerCase().contains(_searchQuery.toLowerCase());
                  final matchesStatus = _statusFilter == 'all' ||
                      (_statusFilter == 'active' && prod.active) ||
                      (_statusFilter == 'inactive' && !prod.active);
                  final matchesCategory = _categoryFilter == 'all' || prod.category == _categoryFilter;
                  return matchesSearch && matchesStatus && matchesCategory;
                }).toList();

                if (filtered.isEmpty) {
                  return const Center(child: Text('No products match criteria', style: TextStyle(color: Colors.white55)));
                }

                return ListView.builder(
                  padding: const EdgeInsets.symmetric(horizontal: 12, vertical: 4),
                  itemCount: filtered.length,
                  itemBuilder: (context, index) {
                    final product = filtered[index];
                    return _buildProductRow(product);
                  },
                );
              },
            ),
          ),
        ],
      ),
    );
  }

  Widget _buildFilterChip(String label, String value) {
    final isSelected = _statusFilter == value;
    return GestureDetector(
      onTap: () {
        setState(() {
          _statusFilter = value;
        });
      },
      child: Container(
        padding: const EdgeInsets.symmetric(horizontal: 12, vertical: 6),
        decoration: BoxDecoration(
          color: isSelected ? const Color(0xFFC5A059) : Colors.transparent,
          borderRadius: BorderRadius.circular(8),
          border: Border.all(
            color: isSelected ? const Color(0xFFC5A059) : Colors.white24,
            width: 0.8,
          ),
        ),
        child: Text(
          label,
          style: TextStyle(
            color: isSelected ? Colors.black : Colors.white70,
            fontSize: 11,
            fontWeight: FontWeight.bold,
          ),
        ),
      ),
    );
  }

  Widget _buildProductRow(Product product) {
    return Card(
      margin: const EdgeInsets.only(bottom: 12),
      child: Padding(
        padding: const EdgeInsets.all(10.0),
        child: Row(
          children: [
            // Image Box
            ClipRRect(
              borderRadius: BorderRadius.circular(10),
              child: SizedBox(
                width: 72,
                height: 72,
                child: product.imageUrl.isNotEmpty
                    ? CachedNetworkImage(
                        imageUrl: product.imageUrl,
                        fit: BoxFit.cover,
                        placeholder: (context, url) => Container(color: Colors.white12),
                        errorWidget: (context, url, error) => Container(
                          color: const Color(0xFFC5A059).withOpacity(0.1),
                          child: const Icon(Icons.image, color: Color(0xFFC5A059), size: 28),
                        ),
                      )
                    : Container(
                        color: const Color(0xFFC5A059).withOpacity(0.1),
                        child: const Icon(Icons.image, color: Color(0xFFC5A059), size: 28),
                      ),
              ),
            ),
            const SizedBox(width: 12),
            // Info Column
            Expanded(
              child: Column(
                crossAxisAlignment: CrossAxisAlignment.start,
                children: [
                  Text(
                    product.name,
                    style: const TextStyle(fontWeight: FontWeight.bold, fontSize: 13.5),
                    maxLines: 1,
                    overflow: TextOverflow.ellipsis,
                  ),
                  const SizedBox(height: 3),
                  Text(
                    '${product.price.toInt()} ETB • Stock: ${product.stock}',
                    style: const TextStyle(color: Color(0xFFC5A059), fontSize: 11.5, fontWeight: FontWeight.bold),
                  ),
                  const SizedBox(height: 5),
                  Row(
                    children: [
                      Container(
                        padding: const EdgeInsets.symmetric(horizontal: 6, vertical: 2),
                        decoration: BoxDecoration(
                          color: Colors.white12,
                          borderRadius: BorderRadius.circular(4),
                        ),
                        child: Text(
                          product.category.toUpperCase(),
                          style: const TextStyle(fontSize: 8, fontWeight: FontWeight.bold, color: Colors.white60),
                        ),
                      ),
                      const SizedBox(width: 6),
                      Container(
                        padding: const EdgeInsets.symmetric(horizontal: 6, vertical: 2),
                        decoration: BoxDecoration(
                          color: product.active ? Colors.green.withOpacity(0.2) : Colors.red.withOpacity(0.2),
                          borderRadius: BorderRadius.circular(4),
                        ),
                        child: Text(
                          product.active ? 'ACTIVE' : 'INACTIVE',
                          style: TextStyle(
                            fontSize: 8,
                            fontWeight: FontWeight.bold,
                            color: product.active ? Colors.greenAccent : Colors.redAccent,
                          ),
                        ),
                      ),
                    ],
                  )
                ],
              ),
            ),
            // Actions Menu
            Column(
              children: [
                IconButton(
                  icon: const Icon(Icons.edit, color: Color(0xFFC5A059), size: 18),
                  onPressed: () {
                    Navigator.push(
                      context,
                      MaterialPageRoute(
                        builder: (context) => AddProductScreen(product: product),
                      ),
                    );
                  },
                ),
                IconButton(
                  icon: const Icon(Icons.delete_sweep_rounded, color: Colors.redAccent, size: 18),
                  onPressed: () => _showDeleteDialog(product),
                ),
              ],
            )
          ],
        ),
      ),
    );
  }

  void _showDeleteDialog(Product product) {
    showDialog(
      context: context,
      builder: (context) {
        return AlertDialog(
          title: const Text('Delete Product?', style: TextStyle(fontWeight: FontWeight.bold)),
          content: Text('Are you sure you want to delete "${product.name}"? This will remove the listing and its associated storage assets permanently.'),
          actions: [
            TextButton(
              onPressed: () => Navigator.pop(context),
              child: const Text('CANCEL', style: TextStyle(color: Colors.white60)),
            ),
            TextButton(
              onPressed: () async {
                Navigator.pop(context);
                try {
                  await _firebaseService.deleteProduct(product.id, product.imageUrl);
                  if (mounted) {
                    ScaffoldMessenger.of(context).showSnackBar(
                      const SnackBar(content: Text('Product deleted successfully.')),
                    );
                  }
                } catch (e) {
                  if (mounted) {
                    ScaffoldMessenger.of(context).showSnackBar(
                      SnackBar(content: Text('Error: $e')),
                    );
                  }
                }
              },
              child: const Text('DELETE', style: TextStyle(color: Colors.redAccent, fontWeight: FontWeight.bold)),
            ),
          ],
        );
      },
    );
  }
}
