import 'package:flutter/material.dart';
import 'package:firebase_core/firebase_core.dart';
import 'package:firebase_auth/firebase_auth.dart';
import 'package:flutter_spinkit/flutter_spinkit.dart';
import 'screens/login/login_screen.dart';
import 'screens/dashboard/dashboard_screen.dart';
import 'services/firebase_service.dart';

void main() async {
  WidgetsFlutterBinding.ensureInitialized();
  // Wrap with try-catch in case it's compiled or run in preview without google-services.json
  try {
    await Firebase.initializeApp();
  } catch (e) {
    debugPrint("Firebase Initialization Error: $e");
  }
  runApp(const MyApp());
}

class MyApp extends StatelessWidget {
  const MyApp({super.key});

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      title: 'Teke Gift Shop Admin',
      debugShowCheckedModeBanner: false,
      themeMode: ThemeMode.system,
      theme: ThemeData(
        useMaterial3: true,
        colorScheme: ColorScheme.fromSeed(
          seedColor: const Color(0xFFC5A059),
          primary: const Color(0xFFC5A059),
          secondary: const Color(0xFF1E1E1E),
          brightness: Brightness.light,
        ),
        cardTheme: CardTheme(
          elevation: 2,
          shape: RoundedCornerShape(16),
        ),
      ),
      darkTheme: ThemeData(
        useMaterial3: true,
        colorScheme: ColorScheme.fromSeed(
          seedColor: const Color(0xFFC5A059),
          primary: const Color(0xFFC5A059),
          secondary: const Color(0xFF0D0D0D),
          brightness: Brightness.dark,
          background: const Color(0xFF0D0D0D),
          surface: const Color(0xFF161616),
        ),
        scaffoldBackgroundColor: const Color(0xFF0D0D0D),
        cardTheme: CardTheme(
          elevation: 4,
          color: const Color(0xFF161616),
          shape: RoundedCornerShape(16),
        ),
      ),
      home: const AuthWrapper(),
    );
  }
}

class RoundedCornerShape extends RoundedRectangleBorder {
  RoundedCornerShape(double radius) : super(borderRadius: BorderRadius.circular(radius));
}

class AuthWrapper extends StatelessWidget {
  const AuthWrapper({super.key});

  @override
  Widget build(BuildContext context) {
    final firebaseService = FirebaseService();
    return StreamBuilder<User?>(
      stream: firebaseService.authStateChanges,
      builder: (context, snapshot) {
        if (snapshot.connectionState == ConnectionState.waiting) {
          return const Scaffold(
            body: Center(
              child: SpinKitDoubleBounce(
                color: Color(0xFFC5A059),
                size: 50.0,
              ),
            ),
          );
        }
        
        if (snapshot.hasData) {
          return const DashboardScreen();
        }
        
        return const LoginScreen();
      },
    );
  }
}
