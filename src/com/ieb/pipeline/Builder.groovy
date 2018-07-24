package com.ieb.pipeline;

void build() {
	
    agent { label 'Worker1' }

    stages {

        stage('Checkout') {
            steps {
                echo 'Checkout source, report conflicts and issues with migrations or grunt'
            }
        }

        stage('UnitTesting') {
            steps {
                echo 'PhpUnit - component testing with mock api stubs'
                sh "composer install"
                sh "./vendor/bin/phpunit --log-junit reports/phpunit.xml --coverage-clover reports/coverage.xml --coverage-html reports/coverage"
            }
        }

        stage('ParallelTesting') {
            steps {
                parallel (
                    "StaticAnalysis" : { echo 'SonarPHP - Codesniffer, LinesOfCode, MessDetector, CopyPaste Detector, CodeBrowser, DOX' },
                    "Integration" : { echo 'BrowserStack with end to end testing' },
                    "LoadTesting" : { echo 'JMeter, Bench, Seige' },
                    "Security" : { echo 'RIPs security scanning' }
                )
            }
        }

        stage('ReleaseToDark') {
            when {
                branch "PR-..*"
            }
            steps {
                echo "Deploy to 'dark' environment"
            }
        }
    }

    post {
        always {
            junit 'reports/*.xml'
            publishHTML target: [
                allowMissing: false,
                alwaysLinkToLastBuild: false,
                keepAll: true,
                reportDir: 'reports/coverage',
                reportFiles: 'index.html',
                reportName: 'Coverage Report'
            ]
        }
    }
}