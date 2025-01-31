/*
 * Copyright 2018-2019 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.springframework.cloud.function.adapter.aws;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cloud.function.web.source.DestinationResolver;
import org.springframework.cloud.function.web.source.FunctionExporterAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Dave Syer
 */
//TODO - do we actually need it?????

@Configuration
@AutoConfigureBefore(FunctionExporterAutoConfiguration.class)
@ConditionalOnClass(DestinationResolver.class)
@ConditionalOnProperty(prefix = "spring.cloud.function.web.export", name = "enabled", matchIfMissing = false)
public class CustomRuntimeAutoConfiguration {

	@Bean
	@ConditionalOnMissingBean
	public DestinationResolver destinationResolver() {
		return new LambdaDestinationResolver();
	}

	@Bean
	public CommandLineRunner backgrounder() {
		return args -> background();
	}

	static void background() {
		Thread thread = new Thread(() -> {
			System.out.println("Started");
			while (true) {
				try {
					Thread.sleep(500L);
				}
				catch (InterruptedException e) {
				}
			}
		});
		thread.setDaemon(false);
		thread.start();
	}

}
