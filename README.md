# 🤖 Smart Notifier - Mobile Cloud Project

Smart Notifier is a machine learning-powered Android application designed to predict human activity in real time using mobile sensors and cloud-trained models. It integrates intelligent notification systems with activity recognition to enhance user engagement and context-aware experiences.

---

## 🌟 Project Overview

This project explores the integration of:

- 🧠 Human Activity Recognition (HAR)
- 📲 Android app development
- ☁️ Cloud-based model training (Kaggle TPU)
- 💡 Smart prompting mechanisms
- 🔁 TensorFlow Lite for edge-device performance

Developed as a final project for **Mobile Cloud Computing**.

---

## 📦 Dataset

**Source**: UCI Machine Learning Repository  
**Name**: Human Activity Recognition Using Smartphones  
- 👤 30 volunteers (ages 19–48)
- 📱 Device: Samsung Galaxy S II
- 📊 Sensors: Accelerometer & Gyroscope
- ⏱️ Sampling Rate: 50Hz  
**Activities Tracked**:
- Walking
- Walking Upstairs
- Walking Downstairs
- Sitting
- Standing
- Laying

---

## 🧪 Model Training & Performance

| Training Method        | Time         | Epochs | Specs                     |
|------------------------|--------------|--------|----------------------------|
| Local (Dell Latitude)  | > 2 hours    | 30     | 16 GB RAM                 |
| Kaggle TPU (Cloud)     | ~40 minutes  | 30     | Cloud TPU acceleration    |

> TensorFlow Lite was used to convert and optimize the model for Android devices.

---

## 🛠️ Tech Stack

- Android (Java/Kotlin)
- TensorFlow Lite
- AWS API
- Fragments & NotificationManager
- Cloud Training (Kaggle TPU)
- Edge Deployment via TFLite

---

## 🔄 Data Flow

- Sensor Input → HAR Model (TFLite) → Activity prediction  
- Prediction → Trigger Notification via Android NotificationManager  
- Chatbot logic powered by "Chat 3.5b" & Smart Prompting  


