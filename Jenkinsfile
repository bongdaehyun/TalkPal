pipeline {
        agent none
        options { skipDefaultCheckout(false) }
        stages {
                stage('Docker build') {
                        agent any
                        steps {
                                sh 'docker build -t front ./front'
                                sh 'docker build -t back ./backendpjt'
                        }
                }
                stage('Docker run') {
                        agent any
                        steps {

                                sh 'docker ps -f name=front -q \
                                        | xargs --no-run-if-empty docker container stop'

                                sh 'docker ps -f name=back -q \
                                        | xargs --no-run-if-empty docker container stop'

                                sh 'docker container ls -a -f name=front -q \
                                        | xargs -r docker container rm'

                                sh 'docker container ls -a -f name=back -q \
                                        | xargs -r docker container rm'
                                sh 'docker images -f "dangling=true" -q \
                                        | xargs -r docker rmi'

                                sh 'docker run -d --name front -p 80:80 -p 443:443 -v /home/ubuntu/yoon/cert:/usr/share/nginx/html/homepage/cert \
                                -v ~/volumes/profile:/usr/share/nginx/html/homepage/profile \
                                --network our_net front'
                                sh 'docker run -d --name back -p 8080:8080 -v ~/volumes/profile:/volumes/profile --network our_net back'

                                discordSend(description: "${currentBuild.currentResult}: Job ${env.JOB_NAME} \nBuild: ${env.BUILD_NUMBER} \nMore info at: \n${env.BUILD_URL}", footer: '', unstable: true, link: env.BUILD_URL, result: "${currentBuild.currentResult}", title: "${JOB_NAME} SUCCESS", webhookURL: 'https://discord.com/api/webhooks/870524982504091648/oq_2jOfSRPB527X93MqWAs-IF8i_OI6sn1-ltAmQtSp82cIP-L8z6Ok8unc26wO8mols')
                        }
                }
        }
}
