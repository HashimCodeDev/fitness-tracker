# 🏋️ FitTrack - Personal Fitness Tracker

A modern web application that helps users track their workouts, monitor fitness progress, and maintain a healthy lifestyle.

## 📋 What This Project Does

**FitTrack** is a fitness tracking application where users can:
- ✅ Create an account and log in securely
- ✅ Record their workouts with exercises, sets, and reps
- ✅ View their workout history and progress over time
- ✅ Track personal information like weight, height, and BMI
- ✅ Get visual charts and statistics of their fitness journey

## 🛠️ Technology Stack

### Backend (Server-side)
- **Java 17** - Programming language
- **Spring Boot 3.5.6** - Framework for building the web application
- **Spring Security** - Handles user authentication and security
- **Spring Data JPA** - Manages database operations
- **PostgreSQL** - Database to store user data and workouts
- **Maven** - Build tool and dependency management

### Frontend (User Interface)
- **HTML5** - Structure of web pages
- **Tailwind CSS** - Modern styling framework
- **JavaScript** - Interactive functionality
- **Chart.js** - Creates workout progress charts

## 📁 Project Structure

```
fitness-tracker/
├── src/main/java/com/example/fitnesstracker/
│   ├── controller/          # Handles web requests (API endpoints)
│   │   ├── AuthController.java       # Login/Register functionality
│   │   ├── UserController.java       # User profile management
│   │   ├── WorkoutController.java    # Workout creation/editing
│   │   └── WorkoutHistoryController.java # Workout history & stats
│   ├── model/              # Data structures (Database tables)
│   │   ├── User.java       # User information (name, email, BMI)
│   │   ├── Workout.java    # Workout sessions
│   │   └── Exercise.java   # Individual exercises in workouts
│   ├── repository/         # Database access layer
│   ├── config/            # Security and app configuration
│   └── dto/               # Data transfer objects
├── src/main/resources/
│   ├── static/index.html  # Main web page (Frontend)
│   └── application.properties # App configuration
└── pom.xml               # Project dependencies and build settings
```

## 🚀 How to Run the Project

### Prerequisites
- **Java 17** installed on your computer
- **PostgreSQL** database running
- **Maven** (or use the included `mvnw` wrapper)

### Steps to Run
1. **Clone the repository**
   ```bash
   git clone <repository-url>
   cd fitness-tracker
   ```

2. **Set up the database**
   - Create a PostgreSQL database named `fitness_tracker`
   - Update database credentials in `src/main/resources/application.properties`

3. **Run the application**
   ```bash
   ./mvnw spring-boot:run
   ```
   Or if you have Maven installed:
   ```bash
   mvn spring-boot:run
   ```

4. **Access the application**
   - Open your web browser
   - Go to `http://localhost:8080`
   - Create an account and start tracking your workouts!

## 🎯 Key Features Explained

### 1. User Management
- **Registration**: New users can create accounts with email and password
- **Login**: Secure authentication using Spring Security
- **Profile**: Users can update their personal information (age, height, weight)
- **BMI Calculation**: Automatically calculates Body Mass Index

### 2. Workout Tracking
- **Create Workouts**: Users can log new workout sessions
- **Add Exercises**: Each workout can contain multiple exercises
- **Track Details**: Record sets, reps, and weights for each exercise
- **Date Tracking**: Workouts are organized by date

### 3. Progress Monitoring
- **Workout History**: View all past workouts in chronological order
- **Statistics**: Get insights about workout frequency and progress
- **Visual Charts**: Interactive graphs showing workout trends over time

## 🔧 Technical Implementation

### Database Design
- **Users Table**: Stores user accounts and personal information
- **Workouts Table**: Contains workout sessions linked to users
- **Exercises Table**: Individual exercises within each workout

### Security Features
- Password encryption using Spring Security
- Session-based authentication
- Protected API endpoints
- Input validation to prevent malicious data

### API Endpoints
- `POST /api/auth/register` - Create new user account
- `POST /api/auth/login` - User login
- `GET /api/workouts` - Get user's workout history
- `POST /api/workouts` - Create new workout
- `GET /api/users/profile` - Get user profile information

## 🎨 User Interface

The frontend features a modern, responsive design with:
- **Dark theme** with gradient backgrounds
- **Mobile-friendly** layout that works on phones and tablets
- **Interactive charts** for visualizing progress
- **Smooth animations** for better user experience
- **Form validation** to ensure data quality

## 📊 What Makes This Project Special

1. **Full-Stack Application**: Combines backend API with interactive frontend
2. **Modern Technologies**: Uses current industry-standard frameworks
3. **Secure**: Implements proper authentication and data protection
4. **User-Friendly**: Intuitive interface that's easy to navigate
5. **Scalable**: Architecture supports adding new features easily

## 🎓 Learning Outcomes

This project demonstrates understanding of:
- **Web Development**: Building complete web applications
- **Database Design**: Creating relational database schemas
- **API Development**: Building RESTful web services
- **Security**: Implementing user authentication and authorization
- **Frontend Development**: Creating responsive, interactive user interfaces
- **Software Architecture**: Organizing code using MVC pattern

## 🔮 Future Enhancements

Potential features that could be added:
- Exercise templates and workout plans
- Social features (sharing workouts with friends)
- Mobile app version
- Integration with fitness devices
- Nutrition tracking
- Goal setting and achievement badges

---

**Built with ❤️ using Spring Boot and modern web technologies**