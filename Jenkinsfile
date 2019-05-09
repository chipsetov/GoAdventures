pipeline {
  agent none
  stages {
    stage('make image and push to dockerhub') {
      agent any
      steps {
        git(url: 'https://github.com/chipsetov/GoAdventures', branch: 'develop')
        sh 'cd client && docker build -t sgsh/goad-app --no-cache .'
        sh 'cd client && docker push sgsh/react-app:latest'
        sh 'cd client && docker rmi -f react-app sgsh/react-app'
      }
    }
    stage('build & SonarQube analysis') {
      agent {
        docker {
          image 'maven:3.6-jdk-11'
          args '--network=jenkinsnet -v /var/run/docker.sock:/var/run/docker.sock'
        }

      }
      steps {
        git(url: 'https://github.com/chipsetov/GoAdventures', branch: 'develop')
        sh 'cd server/goadventures && mvn package -DskipTests=true -Dmaven.javadoc.skip=true -B -V'
        sh 'cd server/goadventures && mvn dependency:go-offline'
        sh 'cd server/goadventures && mvn install -DskipTests=true -Dmaven.javadoc.skip=true -B -V'
        withSonarQubeEnv('sonarqubee') {
          sh 'cd server/goadventures && mvn sonar:sonar'
        }

      }
    }
    stage('Client') {
      agent {
        docker {
          image 'node:8.10.0'
        }

      }
      steps {
        git(url: 'https://github.com/chipsetov/GoAdventures', branch: 'develop')
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
        sh 'cd server/goadventures && mvn dependency:go-offline'
        sh 'cd server/goadventures && mvn clean validate'
        sh 'cd server/goadventures && mvn clean compile'
        sh 'cd server/goadventures && mvn install -DskipTests=true -Dmaven.javadoc.skip=true -B -V'
      }
    }
  }
  environment {
    registry = 'sgsh/go-ad'
    registryCredential = 'dockerhub'
    dockerfilemavenversion = '1'
  }
  post {
    success {
      echo 'I succeeeded!'
      mail(subject: "${env.JOB_NAME}-${env.BUILD_NUMBER}", body: 'GoAdventures build', to: 'shakh.softgroup@gmail.com')
      slackSend(channel: '#jenkins', color: '#00FF00', message: "*${currentBuild.currentResult}:* Job ${env.JOB_NAME} build ${env.BUILD_NUMBER}\n More info at: ${env.BUILD_URL}")

    }

    failure {
      echo 'I failed :('
      mail(subject: "${env.JOB_NAME}-${env.BUILD_NUMBER}", body: 'GoAdventures failed', to: 'shakh.softgroup@gmail.com')
      slackSend(channel: '#jenkins', color: '#FF0000', message: "*${currentBuild.currentResult}:* Job ${env.JOB_NAME} build ${env.BUILD_NUMBER}\n More info at: ${env.BUILD_URL}")

    }

  }
}