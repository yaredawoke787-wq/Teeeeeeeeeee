import 'package:flutter/material.dart';
import '../../services/firebase_service.dart';
import '../../models/category.dart';

class CategoriesScreen extends StatefulWidget {
  const CategoriesScreen({super.key});

  @override
  State<CategoriesScreen> createState() => _CategoriesScreenState();
}

class _CategoriesScreenState extends State<CategoriesScreen> {
  final _firebaseService = FirebaseService();
  final _nameController = TextEditingController();

  @override
  void dispose() {
    _nameController.dispose();
    super.dispose();
  }

  void _showCategoryDialog({CategoryModel? category}) {
    _nameController.text = category?.name ?? '';
    
    showDialog(
      context: context,
      builder: (context) {
        return AlertDialog(
          title: Text(category == null ? 'ADD CATEGORY' : 'EDIT CATEGORY', style: const TextStyle(fontWeight: FontWeight.bold, fontSize: 16)),
          content: TextField(
            controller: _nameController,
            decoration: InputDecoration(
              hintText: 'Category Name',
              border: OutlineInputBorder(borderRadius: BorderRadius.circular(10)),
            ),
          ),
          actions: [
            TextButton(
              onPressed: () => Navigator.pop(context),
              child: const Text('CANCEL', style: TextStyle(color: Colors.white60)),
            ),
            TextButton(
              onPressed: () async {
                final name = _nameController.text.trim();
                if (name.isEmpty) return;

                Navigator.pop(context);

                final c = CategoryModel(
                  id: category?.id ?? '',
                  name: name,
                  imageUrl: category?.imageUrl ?? '',
                  createdAt: category?.createdAt ?? DateTime.now(),
                );

                try {
                  if (category == null) {
                    await _firebaseService.addCategory(c);
                  } else {
                    await _firebaseService.updateCategory(c);
                  }
                  if (mounted) {
                    ScaffoldMessenger.of(context).showSnackBar(
                      const SnackBar(content: Text('Category successfully updated.')),
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
              child: const Text('SAVE', style: TextStyle(color: Color(0xFFC5A059), fontWeight: FontWeight.bold)),
            ),
          ],
        );
      },
    );
  }

  void _showDeleteConfirm(CategoryModel category) {
    showDialog(
      context: context,
      builder: (context) {
        return AlertDialog(
          title: const Text('Delete Category?', style: TextStyle(fontWeight: FontWeight.bold)),
          content: Text('Are you sure you want to delete the category "${category.name}"? Products inside this category will still remain but their category filter won\'t resolve.'),
          actions: [
            TextButton(
              onPressed: () => Navigator.pop(context),
              child: const Text('CANCEL', style: TextStyle(color: Colors.white60)),
            ),
            TextButton(
              onPressed: () async {
                Navigator.pop(context);
                try {
                  await _firebaseService.deleteCategory(category.id);
                  if (mounted) {
                    ScaffoldMessenger.of(context).showSnackBar(
                      const SnackBar(content: Text('Category deleted successfully.')),
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

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      floatingActionButton: FloatingActionButton(
        onPressed: () => _showCategoryDialog(),
        backgroundColor: const Color(0xFFC5A059),
        foregroundColor: Colors.black,
        child: const Icon(Icons.add),
      ),
      body: StreamBuilder<List<CategoryModel>>(
        stream: _firebaseService.getCategories(),
        builder: (context, snapshot) {
          if (snapshot.connectionState == ConnectionState.waiting) {
            return const Center(child: CircularProgressIndicator(color: Color(0xFFC5A059)));
          }

          if (snapshot.hasError) {
            return Center(child: Text('Error: ${snapshot.error}', style: const TextStyle(color: Colors.red)));
          }

          final categories = snapshot.data ?? [];

          if (categories.isEmpty) {
            return const Center(child: Text('No categories created yet.', style: TextStyle(color: Colors.white55)));
          }

          return ListView.builder(
            padding: const EdgeInsets.all(12),
            itemCount: categories.length,
            itemBuilder: (context, index) {
              final cat = categories[index];
              return Card(
                margin: const EdgeInsets.only(bottom: 8),
                child: ListTile(
                  leading: Container(
                    width: 36,
                    height: 36,
                    decoration: BoxDecoration(
                      color: const Color(0xFFC5A059).withOpacity(0.12),
                      shape: BoxShape.circle,
                    ),
                    child: const Icon(Icons.category_outlined, color: Color(0xFFC5A059), size: 18),
                  ),
                  title: Text(cat.name, style: const TextStyle(fontWeight: FontWeight.bold, fontSize: 13.5)),
                  trailing: Row(
                    mainAxisSize: MainAxisSize.min,
                    children: [
                      IconButton(
                        icon: const Icon(Icons.edit, color: Color(0xFFC5A059), size: 18),
                        onPressed: () => _showCategoryDialog(category: cat),
                      ),
                      IconButton(
                        icon: const Icon(Icons.delete_sweep_rounded, color: Colors.redAccent, size: 18),
                        onPressed: () => _showDeleteConfirm(cat),
                      ),
                    ],
                  ),
                ),
              );
            },
          );
        },
      ),
    );
  }
}
