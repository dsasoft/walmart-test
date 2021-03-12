pipeline {
  agent any
  stages {
    stage('Build') {
      steps {
        echo 'Build walmart-test'
        sh 'sh run_build_script.sh'
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
  post {
    always {
      archiveArtifacts(artifacts: 'target/demoapp.jar', fingerprint: true)
    }

    failure {
      mail(to: 'ci-team@example.com', subject: "Failed Pipeline ${currentBuild.fullDisplayName}", body: " For details about the failure, see ${env.BUILD_URL}")
    }

  }
}