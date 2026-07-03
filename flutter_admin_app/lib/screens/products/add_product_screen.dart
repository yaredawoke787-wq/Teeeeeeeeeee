import 'dart:io';
import 'package:flutter/material.dart';
import 'package:image_picker/image_picker.dart';
import 'package:firebase_storage/firebase_storage.dart';
import '../../services/firebase_service.dart';
import '../../models/product.dart';
import '../../models/category.dart';

class AddProductScreen extends StatefulWidget {
  final Product? product; // null for Add, not-null for Edit

  const AddProductScreen({super.key, this.product});

  @override
  State<AddProductScreen> createState() => _AddProductScreenState();
}

class _AddProductScreenState extends State<AddProductScreen> {
  final _formKey = GlobalKey<FormState>();
  final _firebaseService = FirebaseService();
  
  // Form controllers
  late TextEditingController _nameController;
  late TextEditingController _priceController;
  late TextEditingController _descController;
  late TextEditingController _detailsController;
  late TextEditingController _stockController;
  
  String? _selectedCategory;
  bool _isActive = true;
  
  // Image assets
  File? _imageFile;
  String _imageUrl = '';
  bool _isUploading = false;
  double _uploadProgress = 0.0;

  @override
  void initState() {
    super.initState();
    final p = widget.product;
    _nameController = TextEditingController(text: p?.name ?? '');
    _priceController = TextEditingController(text: p?.price?.toString() ?? '');
    _descController = TextEditingController(text: p?.description ?? '');
    _detailsController = TextEditingController(text: p?.details ?? '');
    _stockController = TextEditingController(text: p?.stock?.toString() ?? '15');
    _selectedCategory = p?.category;
    _isActive = p?.active ?? true;
    _imageUrl = p?.imageUrl ?? '';
  }

  @override
  void dispose() {
    _nameController.dispose();
    _priceController.dispose();
    _descController.dispose();
    _detailsController.dispose();
    _stockController.dispose();
    super.dispose();
  }

  Future<void> _pickImage() async {
    final picker = ImagePicker();
    final pickedFile = await picker.pickImage(source: ImageSource.gallery, imageQuality: 80);
    
    if (pickedFile != null) {
      setState(() {
        _imageFile = File(pickedFile.path);
      });
      await _uploadSelectedImage();
    }
  }

  Future<void> _uploadSelectedImage() async {
    if (_imageFile == null) return;
    
    setState(() {
      _isUploading = true;
      _uploadProgress = 0.0;
    });

    try {
      final uploadTask = _firebaseService.uploadImage(_imageFile!, 'products');
      
      uploadTask.snapshotEvents.listen((TaskSnapshot snapshot) {
        setState(() {
          _uploadProgress = snapshot.bytesTransferred / snapshot.totalBytes;
        });
      });

      final taskSnapshot = await uploadTask;
      final downloadUrl = await taskSnapshot.ref.getDownloadURL();
      
      setState(() {
        _imageUrl = downloadUrl;
      });

      if (mounted) {
        ScaffoldMessenger.of(context).showSnackBar(
          const SnackBar(content: Text('Image uploaded successfully.')),
        );
      }
    } catch (e) {
      if (mounted) {
        ScaffoldMessenger.of(context).showSnackBar(
          SnackBar(content: Text('Upload failed: $e')),
        );
      }
    } finally {
      setState(() {
        _isUploading = false;
      });
    }
  }

  Future<void> _saveForm() async {
    if (!_formKey.currentState!.validate()) return;
    if (_imageUrl.isEmpty) {
      ScaffoldMessenger.of(context).showSnackBar(
        const SnackBar(content: Text('Please select and upload a product image.')),
      );
      return;
    }
    if (_selectedCategory == null) {
      ScaffoldMessenger.of(context).showSnackBar(
        const SnackBar(content: Text('Please select a category.')),
      );
      return;
    }

    final p = Product(
      id: widget.product?.id ?? '',
      name: _nameController.text.trim(),
      price: double.parse(_priceController.text),
      description: _descController.text.trim(),
      details: _detailsController.text.trim(),
      category: _selectedCategory!,
      stock: int.parse(_stockController.text),
      imageUrl: _imageUrl,
      active: _isActive,
      createdAt: widget.product?.createdAt ?? DateTime.now(),
      updatedAt: DateTime.now(),
    );

    try {
      if (widget.product == null) {
        await _firebaseService.addProduct(p);
        if (mounted) {
          ScaffoldMessenger.of(context).showSnackBar(
            const SnackBar(content: Text('Product added successfully.')),
          );
        }
      } else {
        await _firebaseService.updateProduct(p);
        if (mounted) {
          ScaffoldMessenger.of(context).showSnackBar(
            const SnackBar(content: Text('Product updated successfully.')),
          );
        }
      }
      if (mounted) Navigator.pop(context);
    } catch (e) {
      if (mounted) {
        ScaffoldMessenger.of(context).showSnackBar(
          SnackBar(content: Text('Failed to save product: $e')),
        );
      }
    }
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: Text(widget.product == null ? 'ADD NEW PRODUCT' : 'EDIT PRODUCT'),
      ),
      body: SingleChildScrollView(
        padding: const EdgeInsets.all(20.0),
        child: Form(
          key: _formKey,
          child: Column(
            crossAxisAlignment: CrossAxisAlignment.stretch,
            children: [
              // Image Picker Area
              _buildImagePickerBox(),
              const SizedBox(height: 24),
              // Name EN
              TextFormField(
                controller: _nameController,
                decoration: InputDecoration(
                  labelText: 'Product Name',
                  border: OutlineInputBorder(borderRadius: BorderRadius.circular(12)),
                ),
                validator: (val) => val == null || val.trim().isEmpty ? 'Product name required' : null,
              ),
              const SizedBox(height: 16),
              // Price & Stock Row
              Row(
                children: [
                  Expanded(
                    child: TextFormField(
                      controller: _priceController,
                      keyboardType: TextInputType.number,
                      decoration: InputDecoration(
                        labelText: 'Price (ETB)',
                        border: OutlineInputBorder(borderRadius: BorderRadius.circular(12)),
                      ),
                      validator: (val) {
                        if (val == null || val.trim().isEmpty) return 'Price required';
                        if (double.tryParse(val) == null) return 'Must be numeric';
                        return null;
                      },
                    ),
                  ),
                  const SizedBox(width: 12),
                  Expanded(
                    child: TextFormField(
                      controller: _stockController,
                      keyboardType: TextInputType.number,
                      decoration: InputDecoration(
                        labelText: 'Stock Qty',
                        border: OutlineInputBorder(borderRadius: BorderRadius.circular(12)),
                      ),
                      validator: (val) {
                        if (val == null || val.trim().isEmpty) return 'Stock required';
                        if (int.tryParse(val) == null) return 'Must be integer';
                        return null;
                      },
                    ),
                  ),
                ],
              ),
              const SizedBox(height: 16),
              // Dynamic Category selector
              _buildCategoryDropdown(),
              const SizedBox(height: 16),
              // Short description
              TextFormField(
                controller: _descController,
                maxLines: 2,
                decoration: InputDecoration(
                  labelText: 'Short Description',
                  border: OutlineInputBorder(borderRadius: BorderRadius.circular(12)),
                ),
                validator: (val) => val == null || val.trim().isEmpty ? 'Short description required' : null,
              ),
              const SizedBox(height: 16),
              // Detailed info
              TextFormField(
                controller: _detailsController,
                maxLines: 4,
                decoration: InputDecoration(
                  labelText: 'Detailed Specifications',
                  border: OutlineInputBorder(borderRadius: BorderRadius.circular(12)),
                ),
                validator: (val) => val == null || val.trim().isEmpty ? 'Specifications required' : null,
              ),
              const SizedBox(height: 16),
              // Status switches
              SwitchListTile(
                title: const Text('Store Visibility Status', style: TextStyle(fontWeight: FontWeight.bold, fontSize: 13.5)),
                subtitle: const Text('Toggle to make active or inactive inside catalogs', style: TextStyle(fontSize: 11)),
                value: _isActive,
                onChanged: (val) {
                  setState(() {
                    _isActive = val;
                  });
                },
                activeColor: const Color(0xFFC5A059),
              ),
              const SizedBox(height: 32),
              // Action Buttons
              Row(
                children: [
                  Expanded(
                    child: OutlinedButton(
                      onPressed: () => Navigator.pop(context),
                      style: OutlinedButton.styleFrom(
                        padding: const EdgeInsets.symmetric(vertical: 14),
                        shape: RoundedRectangleBorder(borderRadius: BorderRadius.circular(12)),
                      ),
                      child: const Text('CANCEL', style: TextStyle(color: Colors.white70)),
                    ),
                  ),
                  const SizedBox(width: 12),
                  Expanded(
                    child: ElevatedButton(
                      onPressed: _isUploading ? null : _saveForm,
                      style: ElevatedButton.styleFrom(
                        backgroundColor: const Color(0xFFC5A059),
                        foregroundColor: Colors.black,
                        padding: const EdgeInsets.symmetric(vertical: 14),
                        shape: RoundedRectangleBorder(borderRadius: BorderRadius.circular(12)),
                      ),
                      child: const Text('SAVE LISTING', style: TextStyle(fontWeight: FontWeight.bold)),
                    ),
                  ),
                ],
              )
            ],
          ),
        ),
      ),
    );
  }

  Widget _buildImagePickerBox() {
    return GestureDetector(
      onTap: _isUploading ? null : _pickImage,
      child: Container(
        height: 170,
        decoration: BoxDecoration(
          color: Colors.white.withOpacity(0.04),
          borderRadius: BorderRadius.circular(16),
          border: Border.all(color: const Color(0xFFC5A059).withOpacity(0.3), width: 1.5),
        ),
        child: Stack(
          alignment: Alignment.center,
          children: [
            if (_imageFile != null)
              ClipRRect(
                borderRadius: BorderRadius.circular(14),
                child: Image.file(_imageFile!, width: double.infinity, height: double.infinity, fit: BoxFit.cover),
              )
            else if (_imageUrl.isNotEmpty)
              ClipRRect(
                borderRadius: BorderRadius.circular(14),
                child: Image.network(_imageUrl, width: double.infinity, height: double.infinity, fit: BoxFit.cover),
              ),
            if (_isUploading)
              Container(
                color: Colors.black54,
                child: Column(
                  mainAxisAlignment: MainAxisAlignment.center,
                  children: [
                    const CircularProgressIndicator(color: Color(0xFFC5A059)),
                    const SizedBox(height: 12),
                    Text('Uploading Image: ${(_uploadProgress * 100).toInt()}%', style: const TextStyle(color: Colors.white, fontWeight: FontWeight.bold)),
                  ],
                ),
              )
            else if (_imageFile == null && _imageUrl.isEmpty)
              Column(
                mainAxisAlignment: MainAxisAlignment.center,
                children: const [
                  Icon(Icons.cloud_upload_outlined, size: 40, color: Color(0xFFC5A059)),
                  SizedBox(height: 10),
                  Text('Upload Product Image', style: TextStyle(fontWeight: FontWeight.bold, fontSize: 13.5)),
                  SizedBox(height: 4),
                  Text('Pick file from device gallery', style: TextStyle(fontSize: 10, color: Colors.white38)),
                ],
              )
          ],
        ),
      ),
    );
  }

  Widget _buildCategoryDropdown() {
    return StreamBuilder<List<CategoryModel>>(
      stream: _firebaseService.getCategories(),
      builder: (context, snapshot) {
        final categories = snapshot.data ?? [];
        return DropdownButtonFormField<String>(
          value: _selectedCategory,
          decoration: InputDecoration(
            labelText: 'Product Category',
            border: OutlineInputBorder(borderRadius: BorderRadius.circular(12)),
          ),
          onChanged: (val) {
            setState(() {
              _selectedCategory = val;
            });
          },
          items: categories.map((cat) {
            return DropdownMenuItem<String>(
              value: cat.id,
              child: Text(cat.name),
            );
          }).toList(),
          validator: (val) => val == null ? 'Please select a category' : null,
        );
      },
    );
  }
}
