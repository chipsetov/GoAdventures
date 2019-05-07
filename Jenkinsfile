pipeline {
  agent {
    docker {
      image 'maven:3.6-jdk-8'
      args '--network=bluessssssss_sonarnet'
    }

  }
  stages {
    stage('SCM') {
      steps {
        checkout scm
        sh 'cd server/goadventures && mvn dependency:go-offline'
        withSonarQubeEnv('sonarqubee') {
          sh 'cd server/goadventures && mvn sonar:sonar'
        }

      }
    }
    stage('build & SonarQube analysis') {
      agent {
        docker {
          image 'maven:3.6-jdk-11'
        }

      }
      steps {
        git(url: 'https://github.com/chipsetov/GoAdventures', branch: 'develop')
        withSonarQubeEnv('sonarqubee') {
          sh 'cd server/goadventures && mvn clean package -Psonar sonar:sonar -DskipTests=true -Dsonar.host.url=http://sonarqube:9000 -X'
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
}