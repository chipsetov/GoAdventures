pipeline {
  agent none
  stages {
    stage('build & SonarQube analysis') {
      agent {
        docker {
          image 'maven:3.6-jdk-11'
          args '--network=jenkinsnet'
        }

      }
      steps {
        git(url: 'https://github.com/chipsetov/GoAdventures', branch: 'develop')
        sh 'cd server/goadventures && mvn dependency:go-offline'
        sh 'cd server/goadventures && mvn install -DskipTests=true -Dmaven.javadoc.skip=true -B -V'
        withSonarQubeEnv('sonarqubee') {
          sh 'cd server/goadventures && mvn sonar:sonar'
        }

      }
    }
    stage('Quality Gate') {
      steps {
        timeout(time: 1, unit: 'HOURS') {
          waitForQualityGate true
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
    stage('notifications') {
      steps {
        mail(subject: "${env.JOB_NAME}-${env.BUILD_NUMBER}", body: 'GoAdventures build', to: 'shakh.softgroup@gmail.com')
        slackSend(channel: '#jenkins', color: 'good', message: "*${currentBuild.currentResult}:* Job ${env.JOB_NAME} build ${env.BUILD_NUMBER}\n More info at: ${env.BUILD_URL}")
      }
    }
  }
  post {
    success {
      echo 'I succeeeded!'
      slackSend(channel: '#jenkins', color: '#00FF00', message: "*${currentBuild.currentResult}:* Job ${env.JOB_NAME} build ${env.BUILD_NUMBER}\n More info at: ${env.BUILD_URL}")

    }

    failure {
      echo 'I failed :('
      slackSend(channel: '#jenkins', color: '#FF0000', message: "*${currentBuild.currentResult}:* Job ${env.JOB_NAME} build ${env.BUILD_NUMBER}\n More info at: ${env.BUILD_URL}")

    }

  }
}