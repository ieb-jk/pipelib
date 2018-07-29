package com.ieb.pipeline;

void build() {
	
	stage("Build Application") {
		echo "We are again in the library"
	}
	
	stage("UnitTesting") {
		echo 'PhpUnit - component testing with mock api stubs'
		sh "echo 'running composer install'"
		sh "echo 'now we would run ./vendor/bin/phpunit --log-junit reports/phpunit.xml --coverage-clover reports/coverage.xml --coverage-html reports/coverage'"
	}
	
	stage('ParallelTesting') {
		parallel (
			"StaticAnalysis" : { echo 'SonarPHP - Codesniffer, LinesOfCode, MessDetector, CopyPaste Detector, CodeBrowser, DOX' },
			"Integration" : { echo 'BrowserStack with end to end testing' },
			"LoadTesting" : { echo 'JMeter, Bench, Seige' },
			"Security" : { echo 'RIPs security scanning' }
		)
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
