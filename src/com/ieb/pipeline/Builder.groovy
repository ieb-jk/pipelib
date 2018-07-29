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
}
