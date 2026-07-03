import 'dart:io';
import 'package:firebase_auth/firebase_auth.dart';
import 'package:cloud_firestore/cloud_firestore.dart';
import 'package:firebase_storage/firebase_storage.dart';
import '../models/product.dart';
import '../models/category.dart';

class FirebaseService {
  final FirebaseAuth _auth = FirebaseAuth.instance;
  final FirebaseFirestore _db = FirebaseFirestore.instance;
  final FirebaseStorage _storage = FirebaseStorage.instance;

  // --- Auth Service ---
  Stream<User?> get authStateChanges => _auth.authStateChanges();
  User? get currentUser => _auth.currentUser;

  Future<UserCredential> signIn(String email, String password) async {
    return await _auth.signInWithEmailAndPassword(email: email, password: password);
  }

  Future<void> signOut() async {
    await _auth.signOut();
  }

  // --- Products Service ---
  Stream<List<Product>> getProducts() {
    return _db.collection('products')
        .orderBy('createdAt', descending: true)
        .snapshots()
        .map((snapshot) => snapshot.docs.map((doc) => Product.fromFirestore(doc)).toList());
  }

  Future<void> addProduct(Product product) async {
    await _db.collection('products').add(product.toFirestore());
  }

  Future<void> updateProduct(Product product) async {
    await _db.collection('products').doc(product.id).update(product.toFirestore());
  }

  Future<void> deleteProduct(String id, String imageUrl) async {
    await _db.collection('products').doc(id).delete();
    // Try to delete from storage if there is a URL
    if (imageUrl.isNotEmpty && imageUrl.contains('firebasestorage.googleapis.com')) {
      try {
        await _storage.refFromURL(imageUrl).delete();
      } catch (e) {
        // Log or handle error if image not found in storage
      }
    }
  }

  // --- Categories Service ---
  Stream<List<CategoryModel>> getCategories() {
    return _db.collection('categories')
        .orderBy('createdAt', descending: true)
        .snapshots()
        .map((snapshot) => snapshot.docs.map((doc) => CategoryModel.fromFirestore(doc)).toList());
  }

  Future<void> addCategory(CategoryModel category) async {
    await _db.collection('categories').add(category.toFirestore());
  }

  Future<void> updateCategory(CategoryModel category) async {
    await _db.collection('categories').doc(category.id).update(category.toFirestore());
  }

  Future<void> deleteCategory(String id) async {
    await _db.collection('categories').doc(id).delete();
  }

  // --- Storage Image Upload ---
  UploadTask uploadImage(File file, String folder) {
    String fileName = '${DateTime.now().millisecondsSinceEpoch}.jpg';
    Reference ref = _storage.ref().child(folder).child(fileName);
    return ref.putFile(file);
  }
}
