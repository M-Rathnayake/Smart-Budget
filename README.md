## Smart Budget
Smart Budget is a user-friendly Kotlin-based finance tracker app designed to help you manage income, expenses, and savings effortlessly. With five intuitive tabs—Dashboard (financial overview), Transactions (add transactions based on categories/edit/delete transactions), Categories (custom spending groups), Budget (set monthly budget limits with alerts), and Backup (secure data export/import)—it ensures organized money management. The app meets all IT2010 Lab Exam 03 requirements, including SharedPreferences for data persistence, Internal Storage for backups, and Notifications for budget warnings. Simple, functional, and clean, Smart Budget turns financial tracking into a seamless habit.

## Core features
1.Transaction Management
o Users can add, edit, and delete income and expense transactions.
o Each transaction must include title, amount, category, and date.

2.Category-wise Spending Analysis
o Users can categorize transactions (e.g., Food, Transport, Bills, Entertainment, etc.).
o The app should display a summary of expenses per category.

3.Monthly Budget Setup
o Users can set a monthly budget and track their progress.
o Display a warning when spending nears or exceeds the budget limit.

4.Data Persistence using SharedPreferences
o Save user preferences such as currency type and budget settings.
o Maintain a simple transaction history that persists across app restarts.

## Bonus Features
1. Data Backup using Internal Storage
o Allow users to export their transaction data as a text file or JSON.
o Provide an option to restore data from a backup.

2. Push Notifications for Budget Alerts
o Notify users when they approach or exceed their set monthly budget.
o Optional reminders to record daily expenses.

## Technical Requirements covered
• The app is developed in Kotlin.
• UI is designed using XML layouts with a focus on simplicity and usability.
• Use of SharedPreferences for data persistence.
• Implemented Internal Storage for backup functionality.
• Notifications are implemented using Android’s Notification Manager.

[![Kotlin](https://img.shields.io/badge/Kotlin-Used-7F52FF?logo=kotlin&logoColor=white)](https://kotlinlang.org/)
