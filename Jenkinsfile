def var
def branch
def pr
node {
  try{
    stage("Prepare"){
       try{
            branch = CHANGE_BRANCH
            pr = true
       }
       catch(Exception e){
             branch = BRANCH_NAME
       }
      git(
          url: 'https://github.com/pjcalvo84/mastercloudapps-cicd.git',
          branch: branch
        )
    }
    stage("Create jar"){
        sh 'mvn clean package -B -DskipTests'
    }
    stage("Test") {
        sh 'mvn test'
   }
   stage("Quality"){
        if(pr)
           sh "mvn sonar:sonar  -Dsonar.pullrequest.branch=${CHANGE_BRANCH} -Dsonar.pullrequest.key=${CHANGE_ID}"
         else
            sh "mvn sonar:sonar  -Dsonar.branch=${branch}"
   }
   stage("Save jar"){
       archiveArtifacts "target/*.jar"
   }
   stage('Publish') {
      sh 'mvn deploy -DskipTests'
      }
  }
   finally {
      junit "target/*-reports/TEST-*.xml"
    }
}
