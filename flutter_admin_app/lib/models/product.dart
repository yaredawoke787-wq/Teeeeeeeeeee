import 'package:cloud_firestore/cloud_firestore.dart';

class Product {
  final String id;
  final String name;
  final double price;
  final String description;
  final String details;
  final String category;
  final int stock;
  final String imageUrl;
  final bool active;
  final DateTime? createdAt;
  final DateTime? updatedAt;

  Product({
    required this.id,
    required this.name,
    required this.price,
    required this.description,
    required this.details,
    required this.category,
    required this.stock,
    required this.imageUrl,
    required this.active,
    this.createdAt,
    this.updatedAt,
  });

  factory Product.fromFirestore(DocumentSnapshot doc) {
    Map<String, dynamic> data = doc.data() as Map<String, dynamic>;
    return Product(
      id: doc.id,
      name: data['name'] ?? '',
      price: (data['price'] ?? 0.0).toDouble(),
      description: data['description'] ?? '',
      details: data['details'] ?? '',
      category: data['category'] ?? '',
      stock: data['stock'] ?? 0,
      imageUrl: data['imageUrl'] ?? '',
      active: data['active'] ?? true,
      createdAt: data['createdAt'] != null ? (data['createdAt'] as Timestamp).toDate() : null,
      updatedAt: data['updatedAt'] != null ? (data['updatedAt'] as Timestamp).toDate() : null,
    );
  }

  Map<String, dynamic> toFirestore() {
    return {
      'name': name,
      'price': price,
      'description': description,
      'details': details,
      'category': category,
      'stock': stock,
      'imageUrl': imageUrl,
      'active': active,
      'createdAt': createdAt != null ? Timestamp.fromDate(createdAt!) : FieldValue.serverTimestamp(),
      'updatedAt': Timestamp.fromDate(updatedAt ?? DateTime.now()),
    };
  }
}
