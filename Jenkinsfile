pipeline {
    agent any
    tools {
            maven 'Maven 3.9.6'
        }
    stages {
        stage('Checkout') {
            steps {
                checkout scm
            }
        }
        stage('Build') {
            steps {
                bat 'mvn clean test'
            }
        }
    }
}