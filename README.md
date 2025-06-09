# POCKETPLAN - PERSONAL BUDGETING APP

PocketPlan is straightforward, easy-to-use Android budgeting app that helps people track their savings, manage their spending, and develop better money management practices. An academic project led to the development of the app, which uses goal tracking, spending insights, and an interactive design to promote financial awareness.

## 🎬 How to Run and Use the App
1. Clone the Repository
  - git clone https://github.com/nyanem/PROG7313.PocketPlan.Poe.git
2. Open in Android Studio
  - File → Open → Select the project folder
  - Let Gradle sync automatically
3. Build and Run
  - Choose an emulator or connect your Android device
  - Click the green "Run" ▶️ button

## 📲 Using the App
Once the app is installed and running:

1. Register and Create Profile
- On the first launch, register a new user.
- Set up your user profile with personal info and preferred settings.

2. Manage Categories
- Use CategoryFragment to add, rename, or remove expense categories.

3. Track Goals
- Head to GoalsFragment to set savings or budget goals.
- Monitor your progress visually.

4. Add Transactions
- Navigate to the "Add Transaction" page.
- Enter amount, select a category, choose a date, and optionally upload a receipt.
- Tap Save to store the transaction.

5. View Reports
- Go to the Reports section.
- Visual charts display your total spending and total savings.

6. Earn Rewards
- Check RewardsFragment to see points or achievements unlocked by reaching goals or saving money.

7. Access Tips and Help
- Visit the Tips and Help pages for financial guidance and app usage tips.

8. Change Language or Currency
- Use the Language/Currency settings to switch between supported languages and currencies — no need to change device settings.
  
## 🚀Features
- 📋 User Registration & Login
- 🧾 Add Expenses – with amount, date and category
- 📷 Attach Receipts (optional image upload)
- 📈 Expense Categories (e.g Groceries, Transport)
- 📊 Monthly Budget Goal and Category - Specific Limits
- 💸 Dashboard ( showing daily spending and progress against goals ) 
- 📈 Daily Spending and Saving Graph (in progress)
- 🏆 Gamification & Rewards (planned)
- 🌐 Database Sync
- ❔ Tips and Tricks 
- 🗣️ Multi-language Support
- 💱 Currency Support

## Video Demonstration Link 


## 🔧 Tech Stack 
- Kotlin (Main Language)
- Android SDK
- Room Database
- XML Layouts
- Firebase

## 📁 Project Structure 
- MainActivity.kt: Entry point of the app that hosts the NavHostFragment and manages navigation.
- UserProfileFragment.kt: Fragment that displays user profile details with options to edit preferences.
- HelpFragment.kt, TipsFragment.kt: Static content fragments for help and usage tips.
- Expense.kt: Model class representing individual expense transactions.
- AppDatabase.kt: Room database configuration for storing expenses and user data.
- CategoryFragment.kt: Fragment to manage and customize expense categories.
- CreateUserProfileFragment.kt: Fragment to create or update a user profile during onboarding or in settings.
- GoalsFragment.kt: Fragment for setting financial goals and tracking progress visually.
- LandingFragment.kt: Initial landing screen shown post-login or registration; acts as the user dashboard.
- RegisterFragment.kt: Fragment to handle new user registration with input validation.
- User.kt: Data class containing user details such as name, email, and preferences.
- UserDatabase.kt: Room database configuration specifically for user profiles.
- fragment_*.xml: UI layout files corresponding to each fragment screen.
- ReportsFragment.kt: Displays financial summaries and visual charts like bar/line graphs to analyze expenses.
- RewardsFragment.kt: Shows points or badges earned by achieving goals, encouraging consistent saving habits.
- LanguageFragment.kt: Allows users to change the app’s language and currency without adjusting device-wide settings.

## 🔥 Firebase Database
![Screenshot4-categoryGoals](https://github.com/user-attachments/assets/728519cc-d57f-4931-a887-cb08b87c4b0b)
![Screenshot3-selectedCategories](https://github.com/user-attachments/assets/05334a5c-9ac5-43e0-8825-e330ce1f7aff)
![Screenshot2-surveyData](https://github.com/user-attachments/assets/8f582f08-2b20-4515-bb41-bea545eee47a)
![Image 2025-06-08 at 21 36](https://github.com/user-attachments/assets/e77ee5da-ea9c-4d84-a1c2-8149c9b35cca)
![Screenshot1-userData](https://github.com/user-attachments/assets/1d584d89-cd1c-4af8-9884-2135440749c2)

## 🔄 Changes from part 2
- The predefined/custom category options are showing now when adding a transaction
- Viewing the category totals in a period was fixed, and is now working
- All activities now include navigation
- The images are being stored to the database now
- Navigation bar is now fully functional

## ✨ Differences in the UI from Part 1 Planning
- Rewards button was in the transactions/expenses page, is now in the reports page
- Navigation bar was updated
- Create profile page is now on the same page as the registration page
- User profile page has now become the settings page
- Banking profile page was removed
- Language and Help buttons on first few pages, was not implemented

## 👩🏽‍🤝‍👩🏻 Contributors 
- Aman Adams (ST10290748)
- Zoë April (ST10044690)
- Khanyi Mabuza (ST10026525) 

## 📌 Notes 
- This app is currently in development as part of our University Portfolio of Evidence
- All data stored locally and no real bank or payment integrations are included.
- ChatGpt was used to compile this readme file. 
