def var

node {
  try{
    stage("Prepare"){
      if(CHANGE_BRANCH != null)
      git(
        url: 'https://github.com/pjcalvo84/mastercloudapps-cicd.git',
        branch: CHANGE_BRANCH
      )
      else
      git(
              url: 'https://github.com/pjcalvo84/mastercloudapps-cicd.git',
              branch: BRANCH_NAME
            )
    }
    stage("Create jar"){
        sh 'mvn clean install -B -DskipTests'
    }
    stage("Test") {
        sh 'mvn test'
   }
   stage("Quality"){
       sh "mvn sonar:sonar  -Dsonar.branch=${BRANCH_NAME}"

   }
   stage("Save jar"){
       archiveArtifacts "target/*.jar"
   }
   stage('Publish') {
      sh 'mvn clean deploy -DskipTests'
      }
  }
   finally {
   sh 'ls -la target/'
      junit "target/*-reports/TEST-*.xml"
    }
}
