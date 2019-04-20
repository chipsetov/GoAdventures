pipeline {
  agent none
  stages {
    stage('Client') {
      agent {
        docker {
          image 'node:8.10.0'
        }

      }
      steps {
        git(url: 'https://github.com/chipsetov/GoAdventures', branch: 'develop')
        sh 'ls -la'
        sh 'cd client'
        sh 'cd client && npm install'
        sh 'cd client && npm run build'
      }
    }
    stage('API') {
      agent {
        docker {
          image 'maven:3.6-jdk-11'
        }

      }
      steps {
        git(url: 'https://github.com/chipsetov/GoAdventures', branch: 'develop')
        sh 'cd server/goadventures'
        sh 'cd server/goadventures && ls -la'
        sh 'cd server/goadventures && mvn dependency:go-offline'
        sh 'cd server/goadventures && mvn clean validate'
        sh 'cd server/goadventures && mvn clean compile'
        sh 'cd server/goadventures && mvn install -DskipTests=true -Dmaven.javadoc.skip=true -B -V'
      }
    }
    stage('notifications') {
      steps {
        mail(subject: 'GoAdventures', body: 'GoAdventures build')
      }
    }
  }
}