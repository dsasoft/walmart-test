pipeline {
  agent any
  stages {
    stage('Build') {
      steps {
        echo 'Build walmart-test'
      }
    }

    stage('Linux Test') {
      parallel {
        stage('Linux Test') {
          steps {
            echo 'Run Linux Tests'
          }
        }

        stage('Windows Test') {
          steps {
            echo 'Run Windows Tests'
          }
        }

      }
    }

    stage('Deploy') {
      steps {
        echo 'Depoying artifact'
        input 'Ok to deploy'
      }
    }

    stage('Deploy Production') {
      steps {
        echo 'Deploy to Prod'
      }
    }

  }
}