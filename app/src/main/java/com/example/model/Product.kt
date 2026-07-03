package com.example.model

import androidx.compose.runtime.mutableStateListOf
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "products")
data class Product(
    @PrimaryKey(autoGenerate = true) val id: Int,
    val titleEn: String,
    val titleAm: String,
    val descriptionEn: String,
    val descriptionAm: String,
    val price: Double,
    val rating: Float,
    val categoryId: String,
    val discountPercentage: Int,
    val specificationsEn: List<String>,
    val specificationsAm: List<String>,
    val heroGradientIndex: Int = 0,
    val active: Boolean = true,
    val stock: Int = 15,
    val imageUrl: String = "",
    val videoUrl: String = ""
) {
    fun getTitle(lang: AppLanguage): String = if (lang == AppLanguage.ENGLISH) titleEn else titleAm
    fun getDescription(lang: AppLanguage): String = if (lang == AppLanguage.ENGLISH) descriptionEn else descriptionAm
    fun getSpecs(lang: AppLanguage): List<String> = if (lang == AppLanguage.ENGLISH) specificationsEn else specificationsAm
}

@Entity(tableName = "categories")
data class Category(
    @PrimaryKey val id: String,
    val nameEn: String,
    val nameAm: String,
    val emoji: String,
    val descriptionEn: String,
    val descriptionAm: String
) {
    fun getName(lang: AppLanguage): String = if (lang == AppLanguage.ENGLISH) nameEn else nameAm
    fun getDescription(lang: AppLanguage): String = if (lang == AppLanguage.ENGLISH) descriptionEn else descriptionAm
}

object ProductRepository {
    val categories = listOf(
        Category("all", "All Gifts", "ሁሉም ስጦታዎች", "✨", "Exclusive master collection.", "ሁሉንም ያካተተ ምርጥ ስብስብ።"),
        Category("chocolate", "Chocolates", "ቸኮሌቶች", "🍫", "Artisanal hand-crafted premium cocoa.", "በእጅ የተሰሩ ምርጥ ቸኮሌቶች።"),
        Category("flowers", "Flowers", "አበቦች", "🌹", "Freshly cut royal roses and floral designs.", "ትኩስ የሮያል ጽጌረዳዎች ስብስብ።"),
        Category("anniversary", "Anniversary", "አመታዊ በዓል", "🎁", "Premium bespoke milestone celebrations.", "የጋብቻ እና የልዩ ቀናት ማክበሪያ።"),
        Category("wedding", "Weddings", "የሰርግ ስጦታዎች", "💍", "Luxury marriage hampers and accessories.", "ለሙሽሮች የሚሆን የቅንጦት ስጦታ።"),
        Category("birthday", "Birthdays", "ልደቶች", "🎂", "Spectacular birthday customizable sets.", "ደማቅ የልደት ቀን ስጦታዎች።"),
        Category("customized", "Customized", "ለእርስዎ የተሰራ", "🎀", "Individually laser-engraved luxury goods.", "የእርስዎ ስም የተቀረጸባቸው ስጦታዎች።"),
        Category("corporate", "Corporate", "የድርጅት", "🏢", "Sophisticated corporate brand hampers.", "ለድርጅት አጋሮች የሚሆን ክብር ማሳያ።")
    )

    val products = listOf(
        Product(
            id = 1,
            titleEn = "Royal Emperor Rose & Gold Box",
            titleAm = "የሮያል ንጉሰ ነገስት ወርቃማ ጽጌረዳ",
            descriptionEn = "An extraordinary arrangement of 24 pristine royal red roses displayed alongside custom-imported luxury gold flakes, secured in an elegant, velvet-lined mahogany box.",
            descriptionAm = "24 ምርጥ የሮያል ቀይ ጽጌረዳዎች በልዩ ሁኔታ ከተዘጋጀ ወርቅ ጋር ተጣምረው፣ በጥንቃቄ በተሰራ ከማሆጋኒ እንጨት በተዘጋጀ የቬልቬት ሳጥን ውስጥ የቀረቡ።",
            price = 4500.0,
            rating = 4.9f,
            categoryId = "flowers",
            discountPercentage = 10,
            specificationsEn = listOf("24 Premium Dutch Roses", "24k Edible Gold Accents", "Hand-crafted Mahogany Box", "Satin Gold Ribboning"),
            specificationsAm = listOf("24 ከፍተኛ ጥራት ያላቸው ጽጌረዳዎች", "24 ካራት ወርቅ አባሪ", "በእጅ የተሰራ የማሆጋኒ ሳጥን", "የሐር ወርቃማ ጥብጣብ ማሰሪያ"),
            heroGradientIndex = 0,
            videoUrl = "https://assets.mixkit.co/videos/preview/mixkit-luxury-brand-perfume-bottle-in-studio-41312-large.mp4"
        ),
        Product(
            id = 2,
            titleEn = "Bespoke Royal Anniversary Set",
            titleAm = "የቅንጦት አመታዊ በዓል ስጦታ",
            descriptionEn = "Celebrate lifetime milestones with our signature anniversary collection. Contains premium customized crystal glasses, aged sparkling cider, imported Belgian truffles, and a custom photo album bound in full-grain leather.",
            descriptionAm = "የእርስዎን የፍቅር ጉዞ በሚያስደንቅ የቅንጦት ስጦታ ያክብሩ። የተመረጡ ክሪስታል ብርጭቆዎች፣ ምርጥ አልኮል የሌለው ወይን፣ ከቤልጂየም የመጡ ቸኮሌቶች እና በቆዳ የተለበጠ ፎቶ አልበም የያዘ።",
            price = 6800.0,
            rating = 5.0f,
            categoryId = "anniversary",
            discountPercentage = 15,
            specificationsEn = listOf("2 Personalized Crystal Flutes", "750ml Premium Non-alcoholic Cider", "16pc Assorted Belgian Truffles", "Full-grain Leather Album"),
            specificationsAm = listOf("2 ስም የተቀረጸባቸው ክሪስታል ብርጭቆዎች", "750ሚሊ ልዩ የአልኮል-ነጻ ወይን", "16 ፍሬ የቤልጂየም ቸኮሌት", "በእውነተኛ ቆዳ የተሰራ አልበም"),
            heroGradientIndex = 1,
            videoUrl = "https://assets.mixkit.co/videos/preview/mixkit-luxury-brand-perfume-bottle-in-studio-41312-large.mp4"
        ),
        Product(
            id = 3,
            titleEn = "Ethereal Handcrafted Chocolate Tower",
            titleAm = "ልዩ በእጅ የተሰራ የቸኮሌት ግንብ",
            descriptionEn = "An architectural masterpiece of pure flavor. A 3-tier elegant tower carrying artisanal milk, dark, and white chocolate pralines filled with luxurious pistachio cream and salted caramel.",
            descriptionAm = "ልዩ የጣዕም ጥበብ በሶስት ደረጃ ግንብ የቀረበ። በጥንቃቄ የተሰሩ የፓስታቺዮ ክሬም እና የካራሜል ይዘት ያላቸው የወተት፣ ጥቁር እና ነጭ ቸኮሌቶች ስብስብ።",
            price = 3200.0,
            rating = 4.8f,
            categoryId = "chocolate",
            discountPercentage = 0,
            specificationsEn = listOf("3-Tier Premium Glass Stand", "48 Assorted Hand-crafted Chocolates", "Pistachio & Salted Caramel fillings", "Preservative-free organic cocoa"),
            specificationsAm = listOf("ባለ 3 ደረጃ የቅንጦት መስታወት ማቆሚያ", "48 የተለያዩ በእጅ የተሰሩ ቸኮሌቶች", "የፒስታቺዮ እና ካራሜል ይዘት", "ከተፈጥሮ ኮኮዋ የተዘጋጀ"),
            heroGradientIndex = 2
        ),
        Product(
            id = 4,
            titleEn = "Customized Platinum Ring & Keepsake Case",
            titleAm = "የፕላቲኒየም ቀለበት እና የትዝታ ሳጥን",
            descriptionEn = "Perfect for premium marriage proposals or vows. A sleek, internally lit ring case that reveals the jewelry under a gentle warm-toned spotlight, custom laser-engraved with your names.",
            descriptionAm = "ለጋብቻ ጥያቄዎች የሚሆን ፍጹም ምርጫ። በውስጡ አነስተኛ ማብሪያ ያለው፣ ሳጥኑ ሲከፈት ቀለበቱን በደማቅ ብርሃን የሚያሳይ እና የእርስዎ ስም በሌዘር የተቀረጸበት።",
            price = 8500.0,
            rating = 4.9f,
            categoryId = "wedding",
            discountPercentage = 5,
            specificationsEn = listOf("Sleek Internally-Lit Case", "Custom Laser Name Engraving", "Soft Leatherette Interior lining", "Elegant outer lacquer finish"),
            specificationsAm = listOf("በውስጡ መብራት ያለው ዘመናዊ ሳጥን", "በሌዘር ስም የመቅረጽ አገልግሎት", "ለስላሳ የቆዳ ውስጥ መደረቢያ", "በሚያምር መልኩ የተለቀለቀ ውጫዊ ክፍል"),
            heroGradientIndex = 3
        ),
        Product(
            id = 5,
            titleEn = "Imperial Wedding Special Gift Set",
            titleAm = "የንጉሳዊ የሰርግ ስጦታ ጥቅል",
            descriptionEn = "The ultimate wedding bundle containing personalized gold-plated Ethiopian traditional coffee cups, elegant incense burner (Rekebot theme), customized couple bathrobes, and fine premium perfumes.",
            descriptionAm = "በወርቅ የተለበጡ የሀበሻ ባህላዊ የቡና ሲኒዎች፣ የሚያምር ረከቦት፣ ለባለትዳሮች የሚሆኑ የተንቆጠቆጡ የቤት ውስጥ ልብሶች እና ምርጥ ሽቶዎችን የያዘ የሰርግ ስጦታ።",
            price = 12000.0,
            rating = 5.0f,
            categoryId = "wedding",
            discountPercentage = 12,
            specificationsEn = listOf("12 Personalized Gold Coffee Cups", "Premium Wooden Rekebot Cabinet", "Embroidered Couple Bathrobes", "Exclusive His & Her Perfumes"),
            specificationsAm = listOf("12 በወርቅ የተለበጡ የቡና ሲኒዎች", "በእጅ የተሰራ የእንጨት ረከቦት", "ስም የተጠለፈባቸው የባለትዳር ልብሶች", "የእሱ እና የእሷ ልዩ ሽቶዎች"),
            heroGradientIndex = 4
        ),
        Product(
            id = 6,
            titleEn = "Executive Corporate Mahogany Case",
            titleAm = "የድርጅት ባለስልጣናት ልዩ ስጦታ",
            descriptionEn = "Deliver absolute prestige to key partners. Features a custom metal executive rollerball pen, structured leather organizer, wireless charging power bank wrapped in warm canvas, and a thermal temperature-display thermos.",
            descriptionAm = "ለድርጅት አጋሮች የሚሰጥ ከፍተኛ ክብር መግለጫ። የላቀ የብረት እስክሪብቶ፣ የቆዳ ማስታወሻ ደብተር፣ በጨርቅ የተለበጠ የገመድ አልባ ቻርጀር እና የሙቀት መጠን የሚያሳይ ስማርት ተርሞስ የያዘ።",
            price = 5400.0,
            rating = 4.7f,
            categoryId = "corporate",
            discountPercentage = 0,
            specificationsEn = listOf("Executive Weighted Rollerball Pen", "Wireless Charging Canvas Powerbank", "Full Grain Leather Portfolio Organizer", "Temperature LED Smart Thermos"),
            specificationsAm = listOf("ክብደት ያለው ዘመናዊ የብረት እስክሪብቶ", "የገመድ አልባ ቻርጅ ፖውርባንክ", "ከቆዳ የተሰራ ሰነድ መያዣ", "የሙቀት መጠን የሚያሳይ ዘመናዊ ተርሞስ"),
            heroGradientIndex = 0
        ),
        Product(
            id = 7,
            titleEn = "Golden Premium Birthday Hamper",
            titleAm = "የልደት ቀን ወርቃማ ስጦታ",
            descriptionEn = "A majestic birthday surprise box containing customizable happy birthday wood-carved lights, luxury dark chocolate bar, personalized metal cardholders, and fine dry fruit baskets.",
            descriptionAm = "የልደት ቀን አስገራሚ ስጦታ። መልካም ልደት የሚል የተቀረጸ የእንጨት መብራት፣ ልዩ ጥቁር ቸኮሌት፣ ስም የሚቀረጽበት የብረት ካርድ መያዣ እና ምርጥ ደረቅ ፍራፍሬዎች መያዣን ያካተተ።",
            price = 3900.0,
            rating = 4.8f,
            categoryId = "birthday",
            discountPercentage = 10,
            specificationsEn = listOf("Personalized Wood LED Lamp", "90% Single-origin Dark Chocolate", "Titanium RFID Laser Cardholder", "Natural Dried Premium Fruits Basket"),
            specificationsAm = listOf("የእንጨት የተቀረጸ የልደት LED መብራት", "90% ኦርጋኒክ ጥቁር ቸኮሌት", "የቲታኒየም ስም የተቀረጸበት ካርድ መያዣ", "ምርጥ ደረቅ ፍራፍሬዎች ቅርጫት"),
            heroGradientIndex = 1
        ),
        Product(
            id = 8,
            titleEn = "Satin Ribbon Custom Gift Box",
            titleAm = "የሳቲን ሪበን የልዩ ስጦታ ሳጥን",
            descriptionEn = "Create a custom configuration of your choice inside our flagship satin ribbon gift box. Premium cushioning and luxury packaging materials included to make your selections look otherworldly.",
            descriptionAm = "በሚያምር የሳቲን ጥብጣብ በታሰረ የቅንጦት ሳጥን ውስጥ የሚፈልጉትን ምርቶች በምርጫዎ ያስቀምጡ። ስጦታዎ ፍጹም ማራኪ ሆኖ እንዲታይ የሚያደርግ ልዩ መጠቅለያ።",
            price = 2500.0,
            rating = 4.9f,
            categoryId = "customized",
            discountPercentage = 0,
            specificationsEn = listOf("Extra Large Custom-bound Gift Box", "Premium Silk Interior Cushioning", "Gold Foil Engraved Card", "Bespoke Outer Satin Ribbon Ribboning"),
            specificationsAm = listOf("ትልቅ መጠን ያለው የስጦታ ሳጥን", "የሐር ውስጥ ለስላሳ መደረቢያ", "በወርቅ ቀለም የተጻፈ ካርድ", "በውጪ የታሰረ የሳቲን ሪበን"),
            heroGradientIndex = 2
        )
    )

    // Shared thread-safe runtime state across different activities / views in the same app process
    val liveProducts = mutableStateListOf<Product>()

    val liveCategories = mutableStateListOf<Category>()
}
