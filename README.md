# POCKETPLAN - PERSONAL BUDGETING APP

PocketPlan is straightforward, easy-to-use Android budgeting app that helps people track their savings, manage their spending, and develop better money management practices. An academic project led to the development of the app, which uses goal tracking, spending insights, and an interactive design to promote financial awareness.

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
- ❔ Tips, Tricks and FAQ'S
- 🗣️ Multi-language Support
- 💱 Currency Support

## Video Demonstration Link 
https://youtu.be/ROwj-LSHFOk?si=2TdVdpjVHRAesWZL 

## 🚀 Tech Stack 

- Kotlin (Main Language)
- Android SDK
- Room Database
- XML Layouts

## 📁 Project Structure 

- MainActivity.kt: Entry point of the app
- UserProfile.kt: User profile screen with options
- Help.kt, Tips.kt: Static content/help pages
- Expense.kt: Model class for expenses
- .kt: Room database configuration
- CategoryActivity.kt: Manages expense categories
- CreateUserProfile.kt: Screen to create or update a user profile
- GoalsActivity.kt: Goal setting and progress monitoring
- LandingPage.kt: Landing screen after login/registration
- RegisterActivity.kt: User sign-up screen
- User.kt: Model class for user details
- UserDatabase.kt: Database configuration for storing user profiles
- activity_*.xml: UI layout files
- ReportsActivity.kt: Displays financial reports such as total spending and savings using a bar chart. Helps users visualize their financial habits.
- RewardsActivity.kt: Provides a reward-based system where users earn achievements or points for meeting goals.
- Language.kt: A settings option to switch between supported languages and currencies without the need to change device settings.

## 🔥 Firebase Database
![Screenshot4-categoryGoals](https://github.com/user-attachments/assets/728519cc-d57f-4931-a887-cb08b87c4b0b)


## 🔄 Changes from part 2


## 👩🏽‍🤝‍👩🏻 Contributors 
- Aman Adams (ST10290748)
- Zoë April (ST10044690)
- Khanyi Mabuza (ST10026525) 

## 📌 Notes 
- This app is currently in development as part of our University Portfolio of Evidence
- All data stored locally and no real bank or payment integrations are included.
- ChatGpt was used to compile this readme file. 
