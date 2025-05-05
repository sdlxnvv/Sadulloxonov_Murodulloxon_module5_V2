# Javoblar: 
***
_1. **Thread** - bu jarayon (process) ichidagi eng kichik bajarish birligi. Har bir thread biror vazifani bajaradi va bir jarayon ichida bir nechta thread birgalikda ishlaydi. Thread'lar bir xil xotira va resurslarni ishlatadilar, lekin ular bir-biridan mustaqil ravishda ishlashadi._
***
_2. **Multithreading** va **Multitaskingni** farqi:_

_**Multitasking** - bu bir nechta processlarning bir vaqtning o'zida ishlashi._

_**Multithreading** - bu bitta process ichida bir nechta threadlarning parallel ravishda ishlashi._
***
_3. **Thread Pool (Thread Hovuzi)** - bu ishlash samaradorligini oshirishga mo‘ljallangan mexanizm bo‘lib, threadlarni qayta-qayta yaratish va o‘chirishdan saqlanish uchun oldindan yaratilgan threadlar to‘plami (hovuzi)._
***
## 4. **Callable va Runnable farqi va qachon nima uchun ishlatilinadi**

**`Runnable`** va **`Callable`** - Java'da ko'p oqimlik (multithreading) bajarish uchun ishlatiladigan interfeyslar, lekin ularning farqlari bor:
### **Farqi:**
1. **`Runnable`**:
    - Metod: `run()` (hech qanday qiymat qaytarmaydi).
    - Natija: Vazifani bajaradi, lekin hech qanday qaytish qiymatini qaytarmaydi.
    - Istisnolar: Chegaralangan, checked istisnolarni qaytarib bera olmaydi.

2. **`Callable`**:
    - Metod: `call()` (qiymat qaytaradi).
    - Natija: Vazifani bajaradi va qaytaradigan qiymatni qaytaradi.
    - Istisnolar: Checked istisnolarni tashlashi mumkin.
***
## 5. **Future nima va qachon ishlatiladi**

**`Future`** — bu Java'da asinxron hisoblash natijasini ifodalaydigan interfeys. U multithreading (ko'p oqimlik) muhitida vazifaning natijasi hali tayyor bo'lmasa ham, uni kuzatishga imkon beradi.

### **Asosiy xususiyatlari:**
1. **Natijani olish**: `Future` orqali asinxron vazifaning qaytaradigan qiymatini olish mumkin (`get()` metodi yordamida).
2. **Asinxronlik**: Hisoblash davomida `Future`ga ulanish mumkin va natija tayyor bo‘lishini kutish mumkin.
3. **Vazifa tugashini tekshirish**: `isDone()` metodi orqali vazifa tugagan yoki tugamagani tekshiriladi.
4. **Bekor qilish imkoniyati**: `cancel()` metodi orqali hali bajarilayotgan vazifani bekor qilish mumkin.
***
## **Future va CompetableFuture ning farqi?**
**Future** va **CompletableFuture** farqi:

1. **Future** bloklanadi (`get()`), natijani olish uchun kutadi.
2. **CompletableFuture** bloklanmaydi, zanjirli ishlov berish (`thenApply`, `thenAccept`) va bir nechta vazifani birlashtirish (`allOf`, `anyOf`) imkoniyatini beradi.
3. **Future** oddiy vazifalar uchun mos.
4. **CompletableFuture** murakkab va asinxron operatsiyalar uchun ishlatiladi.
***
## **Volatile non-access modificatori haqida malumot bering. Nima uchun ishlatilinadi**
**`volatile`** — bu non-access modifier, Java-da ko‘p oqimli muhitda o‘zgaruvchi qiymatini **barcha oqimlar uchun ko‘rinuvchan** qilish uchun ishlatiladi.

### **Maqsad:**
- O‘zgaruvchi qiymatini **asosiy xotirada** saqlab, kechikmasdan ma'lumotni yangilaydi (`Visibility`).
- Sinxronizatsiyaga oddiy alternativa, ammo faqat yadro (atomic) o‘qish/yozish operatsiyalari uchun ishlatiladi.

### **Cheklovlar:**
- Kompleks sinxronizatsiya yoki **atomic compound operations** uchun ishlatilmaydi (masalan, `i++`).
***
## **Immutable classlar nima uchun kerak va immutable class yaratishimiz uchun qanday shartlar bajarilishi kerak**
### **Immutable class nima va nima uchun kerak?**

**Immutable class** Java-da o‘zgarmas obyektlarni yaratish uchun ishlatiladi. Bu shunday obyektki, bir marta yaratilgach, uning holati o‘zgartirishga imkon bermaydi. Immutable class:

- **Ma'lumotlar xavfsizligi**ni ta'minlaydi — ko‘p oqimli (multithreading) muhitda ishlaganda obyekt ma'lumotlari himoyalangan bo‘ladi.
- **Caching** uchun mos keladi — o‘zgarmas obyektlarni qayta ishlatish oson, chunki ularning qiymatlari o‘zgarmaydi.
- **Predictable behavior** — obyekt doimo yaratilgan paytdagi qiymatlarini saqlaydi.

---

### **Immutable class yaratish shartlari**

1. **Class final bo‘lishi kerak**:
    - `final` bilan belgilangan class o‘zgartirib bo‘lmaydigan qilib qo‘yadi (extend qilishni cheklaydi).

2. **Fieldlar private va final bo‘lishi kerak**:
    - Field (obyektning holatini belgilovchi o‘zgaruvchilar) tashqaridan to‘g‘ridan-to‘g‘ri o‘zgartirilmasligi uchun `private` bo‘lishi, va qiymatlari o‘zgarmas bo‘lishi uchun `final` bo‘lishi shart.

3. **Setter methodlar bo‘lmasligi kerak**:
    - Fieldni o‘zgartirishga imkon beruvchi `setter` metodlar yaratmaslik kerak.

4. **Fieldlarni tashqi o‘zgartirishlar oldida himoyalash**:
    - Barcha `mutable` obyektli fieldlar uchun ularning nusxasi (`deep copy`) yaratiladi yoki ularga kirishga yo‘l qo‘yilmaydi.

5. **Constructor orqali qiymatlarni o‘rnatish:**
    - Immutable classda qiymatlarni faqat bir marta — constructor orqali o‘rnatish mumkin.

6. **Getter methodlar faqat field o‘zidan yangi obyekt (kopiya) qaytarishi kerak (`deep copy`).**
    - Agar field `mutable` turdan bo‘lsa (masalan, `List`, `Map`), getter bu obyektning nusxasini qaytarishi lozim.
***
## **Asynchrone programming nima?**
**Asynchronous programming** — bu dastur tuzish usuli bo‘lib, unda uzoq davom etadigan vazifalar (masalan, faylni o‘qish, ma'lumotlarni tarmoqqa yuborish) bajarilayotganda dastur boshqa ishlarini davom ettiradi, ya'ni asosiy oqim bloklanmaydi.

---

### **Asosiy xususiyatlari:**
1. **Parallel ishlash:**
    - Dastur uzoq davom etadigan operatsiyalarni kutmasdan boshqa vazifalarni bajarishni davom ettiradi.
2. **Bloklanmaslik (Non-blocking):**
    - Asosiy oqim (main thread) bloklanmaydi, shu sababli dasturning ishlashi to‘xtab qolmaydi.
3. **Yuksak samaradorlik:**
    - Bir vaqtning o‘zida bir nechta jarayonlarni boshqarish orqali dastur samaradorligi oshadi.

---

### **Nima uchun kerak?**
1. **Performance-ni oshirish:**
    - Masalan, katta hajmdagi fayllarni yuklash yoki API chaqiriqlarni bajarishda dastur tezkor ishlashda davom etadi.
2. **Ko‘p foydalanuvchi talablarini bajarish:**
    - Serverlar bir vaqtda bir nechta mijoz so'rovlarini ishlay oladi.
3. **Responsive interfeyslar:**
    - UI dasturlar ekranni muzlatmaydi va foydalanuvchi bilan aloqani saqlab qoladi.

---

### **Asynchronous dasturlash usullari:**
1. **Future/CompletableFuture (Java):**
    - Asinxron vazifalarni rejalashtirish uchun ishlatiladi.
2. **Callback funksiyalar:**
    - Vazifa tugagach, ketma-ket bajariladigan kodni chaqiradi.
3. **Promise/Async-Await (JavaScript):**
    - Kodni soddalashtirish va oson o‘qiladigan suzilmalar uchun.
4. **Threads/Executors:**
    - Oqimlarni boshqarib, parallel vazifalarni bajarish.

---
***
## **Atomic classlar qanday algoritim orqali race conditionni oldini oladi? Yoki thread-safe?**
**Atomic sinflar**, Java util.concurrent paketi tarkibiga kiradigan sinflar bo‘lib, ular race condition (oqimlararo ziddiyat)ni oldini olish uchun **CAS (Compare-And-Swap)** algoritmiga tayanadi. Bu ularni **thread-safe** qilishning asosiy mexanizmini ta'minlaydi.

---

### **CAS (Compare-And-Swap) algoritmi**:

1. **Ma'nosi**:
   CAS — o‘zgaruvchi qiymati asosiy xotiradagi kutilayotgan qiymat bilan mos tushsa, uni yangi qiymatga yangilashga urinish algoritmi.

2. **Ishlash jarayoni**:
    - Dastlabki qiymat (expected value) olingan.
    - O‘zgartirish kerak bo‘lgan yangi qiymat tayinlangan.
    - CAS mexanizmi quyidagini tekshiradi:
        - Agar o‘zgaruvchi qiymati kutilgan qiymatga teng bo‘lsa, uni yangi qiymatga o‘zgartiradi.
        - Aks holda, hech qanday o‘zgarish qilmaydi va qaytadan urinish kerak bo‘ladi.
    - Bu operatsiyalar apparat darajasida **atomik (atomic)** tarzda amalga oshiriladi.

3. **Afzalliklari**:
    - **Critical Section** yoki `synchronized` bloklarga nisbatan samaraliroq.
    - Bu yondashuvda oqimning band bo‘lib qolishi yoki bloklanishi sodir bo‘lmaydi.

---

### **Nima uchun Atomic sinflar thread-safe?**

1. **Atomicity (atomiklikni ta'minlash):**
    - Operatsiyalar bitta bo‘lakda, uzilmas holatda amalga oshadi, ya'ni boshqa oqimlar orasida to‘qnashuv yuz bermaydi.

2. **CAS mexanizmi integral ishlaydi:**
    - Operatsiyalar sinov va yangilash orqali amalga oshiriladi. Agarda qiymat kutilgan qiymat bilan mos tushmasa, vazifa qaytadan boshlanadi.

3. **Synchronized yoki Lock ishlatilmaydi:**
    - Atomic sinflar blokirovkasiz (non-blocking) algoritmlardan foydalanadi, bu esa ko‘p oqimli muhitda samaradorlikni oshiradi.

4. **Thread Interruption yo‘q:**
    - Oqim band bo‘lmaydi, boshqa oqimlar bir vaqtda o‘zgarmas qiymatlar bilan ishlashni davom ettiradi.

---

### **Atomic sinflarda ishlatiladigan asosiy sinflar:**
1. **AtomicInteger**
2. **AtomicLong**
3. **AtomicReference**
4. **AtomicBoolean**
***
## **Serialazition/deserialazition nima?**
**Serialization** — obyektni baytlarga aylantirib, saqlash yoki tarmoq orqali uzatish jarayoni.

**Deserialization** — baytlardan obyektni qayta tiklash jarayoni.

Bu Java'da `Serializable` interfeysidan foydalanib amalga oshiriladi. `transient` kalit so‘zi orqali serialize qilinmaslikni belgilash mumkin.
***
## **Serialable/extrenizeble interfacelarining farqlari nima?**
**Serializable va Externalizable interfeyslarining farqlari:**

1. **Serializable**:
    - Bu marker interfeys (metodlari yo'q).
    - Java avtomatik ravishda barcha maydonlarni (non-`transient`) serialize/deserialize qiladi.
    - Kam nazorat talab qiladi, oson ishlatiladi.

2. **Externalizable**:
    - Bu interfeysda **`readExternal`** va **`writeExternal`** metodlari mavjud.
    - Foydalanuvchi obyektning qaysi maydonlari serialize/deserialize qilinishini to‘liq boshqaradi.
    - Manual boshqaruv, ko‘proq moslashuvchanlik beradi.

**Xulosa:**
- **Serializable**: Avtomatik va oson.
- **Externalizable**: Qo‘lda boshqariladi va batafsil nazorat beradi.
***
## **ReentrentLock haqida malumot bering**
**ReentrantLock** haqida qisqacha:

- `ReentrantLock` — qo‘l bilan boshqariladigan qulf mexanizmi.
- Qulflash uchun: `lock.lock()`.
- Qulfni ochish uchun: `lock.unlock()`.
- Bir `Thread` bir necha marta lock ishlatishi mumkin (reentrantlik xususiyati).
- Fairness (adolat) parametri bilan FIFO tartibini qo‘llab-quvvatlaydi.

**Xulosa**:  
Qulay boshqaruv va ko‘p oqimli dasturlashda katta moslashuvchanlikni ta'minlaydi.
***
## **Logging nima?**
**Logging** — bu dasturlar ishlash jarayonida muhim ma'lumotlarni qayd etish jarayoni.

### Asosiy maqsadi:
- Xatoliklarni tekshirish (debug).
- Dastur faoliyatini kuzatish.
- Audit yoki statistik ma'lumotlarni saqlash.
***
## Daemon thread nima?
**Daemon Thread** — bu fon (background) oqimi bo‘lib, u asosan asosiy oqimga yordam beruvchi vazifalarni bajaradi va **asosiy oqim tugagach avtomatik ravishda to‘xtaydi**.

### Asosiy xususiyatlari:
1. **Fon jarayonlari**ni bajaradi (masalan: `Garbage Collection`, log yozish).
2. Asosiy oqim tugagandan keyin daemon oqimlar avtomatik tugaydi.
3. `setDaemon(true)` orqali oqimni daemon turiga o‘zgartirish mumkin (asosiy oqim ishga tushishidan oldin sozlanishi kerak).
***
## Jar File nima ?
**JAR (Java Archive) file** — bu Java dasturlarini yoki kutubxonalarini bitta arxivlangan fayl sifatida saqlash uchun ishlatiladigan ZIP-formatdagi fayl.

### Asosiy vazifalari:
- Java kod (`.class` fayllarini) bir joyda saqlash.
- Ishga tushiriladigan Java dasturlari (exec-maydoni bilan) yaratish.
- Kutubxonalarni ulash va tarqatish.
***
## Maven nima ?
**Maven** — bu Java loyihalarini boshqarish va avtomatlashtirish uchun ishlatiladigan **loyiha boshqaruv vositasi (build tool)**. U asosan quyidagi vazifalarni bajaradi:

### Asosiy xususiyatlari:
1. **Loyiha tuzilmasini boshqarish**:
   - Maven **standart loyiha tuzilishi** va tavsiyalarni joriy qiladi.
2. **Zaruriy kutubxonalarni boshqarish** (Dependency Management):
   - Loyiha uchun kerakli kutubxonalarni avtomatik yuklab beradi va ularga integratsiya qiladi.
3. **Loyihani qurish va to‘plash** (Build):
   - Loyiha qismlarini birlashtirib, `JAR` yoki `WAR` formatidagi paketlarni yaratadi.
4. **Testlarni avtomatlashtirish**:
   - Loyiha uchun testlarni avtomatik ishga tushirish imkonini beradi.
5. **Multi-loyiha boshqaruvi**:
   - Bir nechta loyihalarni bir vaqtning o‘zida sinxron boshqarish imkonini beradi.
***
## Behavior Parameterization nima?
**Behavior Parameterization** — bu **metodning xatti-harakatini (behavior) parametr sifatida o'tkazish** usuli.

### Asosiy maqsadi:
- Bir xil kodni qayta yozmasdan, uni **moslashuvchan va qayta foydalanish mumkin** qilish.
- Parametr sifatida funksiya yoki lambda ifodasini qabul qilib, har xil vazifalarni bajaradi.

### Foydalanish holatlari:
1. **Lambda expressions** (Java 8+).
2. **Functional Interfaces** (masalan, `Predicate`, `Function`, `Consumer`).
3. **Generic metodlar** bilan moslashuvchanlik.
***
## Declarative va Imperative programming nima?
### **Imperative Programming**:
- **Nima?** Bu yondashuvda, **qanday** qilish kerakligi (jarayon) bosqichlari aniq ko'rsatiladi.
- **Xususiyatlar:**
   - Har bir qadamni batafsil boshqarasiz.
   - Buyruq va oqim nazorati (for, if) yordamida ishlaydi.

### **Declarative Programming**:
- **Nima?** Bu yondashuvda, **nima qilish kerakligi** aytiladi, lekin qanday bajarilishi ko'rsatilmaydi.
- **Xususiyatlar:**
   - Jarayon emas, **natija**ga qaratilgan.
   - Yozilishi osonroq, abstraksiyaga asoslangan (masalan, SQL, Streams API).

**Farqi:**
- **Imperative**: *"Har bir jarayonni qo'lda boshqarishingiz kerak."*
- **Declarative**: *"Maqsadni aytib, bajarilishini tizimga qoldirasiz."*
***
## Stream nima? Stream va Collection larning farqi nimada?
### **Stream nima?**
- **Stream** — bu **ma'lumotlar oqimi** bo'lib, ular ustida **funktsional operatsiyalarni** bajarish uchun ishlatiladi (masalan, filter, map, reduce).
- **Immutability**: Stream ma'lumotlarni o'zgartirmaydi, balki yangi oqim qaytaradi.

### **Stream va Collection farqi:**
1. **Ma'lumotni saqlash:**
   - **Collection**: Ma'lumotlarni saqlaydi.
   - **Stream**: Ma'lumotlarni **bir martalik ishlov berish** uchun oqim sifatida beradi.

2. **Ishlash jarayoni:**
   - **Collection**: Eager (hammasini bir vaqtning o'zida bajaraydi).
   - **Stream**: Lazy (faqat kerakli operatsiyalarni bajaradi).

3. **Iterations:**
   - **Collection**: **External iteration** (tsikl orqali).
   - **Stream**: **Internal iteration** (Stream API avtomatik boshqaradi).