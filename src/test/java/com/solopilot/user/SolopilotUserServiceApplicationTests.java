package com.solopilot.user;

import com.portfolio.service.impl.CloudinaryService;
import com.portfolio.service.ICloudinaryService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.web.multipart.MultipartFile;

@SpringBootTest
@Import(SolopilotUserServiceApplicationTests.TestCloudinaryConfig.class)
class SolopilotUserServiceApplicationTests {

	/**
	 * Test configuration that overrides CloudinaryService for tests.
	 * Provides a dummy implementation that returns a fake URL.
	 */
	@Configuration
	static class TestCloudinaryConfig {

		@Bean
		public ICloudinaryService cloudinaryService() {
			return new ICloudinaryService() {
				@Override
				public String uploadFile(MultipartFile file, String fileName, String folder) {
					// Return a fake URL for testing purposes
					return "https://fake.cloudinary.url/" + folder + "/" + fileName;
				}
			};
		}

		// Optional: If you have CloudinaryService autowired somewhere directly
		@Bean
		public CloudinaryService realCloudinaryService(ICloudinaryService cloudinaryService) {
			// Return a CloudinaryService that uses the fake ICloudinaryService
			return new CloudinaryService(null) {
				@Override
				public String uploadFile(MultipartFile file, String fileName, String folder) {
					return cloudinaryService.uploadFile(file, fileName, folder);
				}
			};
		}
	}

	@Test
	void contextLoads() {
		// ApplicationContext will load successfully
		// You can autowire any service and test methods here without real Cloudinary
	}
}