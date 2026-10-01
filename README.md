# Miami Supermarket — Pay-Point App

An Android application that simulates a supermarket pay-point (checkout) system. Built for **CCS 3211 — Lab Assignment 1** (Department of Computer Science, DeKUT).

## Overview

The app lets a cashier select up to 5 items from a dropdown list, enter quantities, and automatically calculate a running total per item and a grand total. On checkout, it generates an itemized receipt on a separate screen.

## Features

- Dropdown (Spinner) selection for up to 5 items per transaction
- Automatic unit price lookup and per-item total calculation
- Grand total calculation across all selected items
- Input validation:
  - Prevents selecting the same item more than once
  - Requires a valid, positive quantity for each selected item
- Itemized receipt generated on a second screen (`ReceiptActivity`), including quantities, unit prices, and the grand total
- "Back to Checkout" navigation from the receipt screen

## Tech Stack

- **Language:** Java
- **Platform:** Android Studio
- **Min SDK:** 24
- **UI:** XML layouts (`LinearLayout`, `ScrollView`, `Spinner`, `EditText`, `TextView`, `Button`)

## Project Structure

```
app/src/main/java/com/example/miamisupermarket/
├── MainActivity.java       # Checkout screen — item selection, quantity input, total calculation
└── ReceiptActivity.java    # Receipt screen — displays itemized receipt and grand total

app/src/main/res/layout/
├── activity_main.xml       # Checkout screen layout
└── activity_receipt.xml    # Receipt screen layout
```

## How It Works

1. Select an item from each dropdown row (up to 5 rows available).
2. Enter the quantity for each selected item.
3. Tap **Calculate Total** to see per-item and grand totals update.
4. Tap **Generate Receipt** to validate your selections and view the itemized receipt on a new screen.
5. Tap **Back to Checkout** to return and start a new transaction.

## Getting Started

1. Clone the repository:
   ```
   git clone https://github.com/TabbyMacharia/MiamiSupermarket.git
   ```
2. Open the project in Android Studio.
3. Let Gradle sync, then run the app on an emulator or physical device (API 24+).

## Group Members

1. Ruth Ndua
2. Maxwell Chege
3. Lorna Kyalo
4. Esther Kamau
5. Tabitha Macharia

## Course Info

- **Unit:** CCS 3211 Mobile Application Development
- **Assignment:** Lab Assignment 1 (Group Work)
- **Institution:** Dedan Kimathi University of Technology (DeKUT)
