pipeline {
  agent none
  stages {
    stage('Build') {
      agent {
        docker {
          image 'node:8.10.0'
        }

      }
      steps {
        git(url: 'https://github.com/chipsetov/GoAdventures', branch: 'develop')
        sh 'ls -la'
        sh 'cd client'
        sh 'npm install'
        sh 'npm run build'
      }
    }
    stage('Clien') {
      agent {
        docker {
          image 'openjdk:11-jdk'
        }

      }
      steps {
        sh 'cd server/goadventures/'
        sh 'cd server/goadventures/ && mvn dependency:go-offline'
        sh 'cd server/goadventures/ && mvn test'
      }
    }
  }
}