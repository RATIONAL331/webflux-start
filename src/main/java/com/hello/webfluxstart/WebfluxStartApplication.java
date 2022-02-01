package com.hello.webfluxstart;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class WebfluxStartApplication {

	public static void main(String[] args) {
		/**
		 * Blocking call! java.io.RandomAccessFile#readBytes ========================================> 근본적인 원인(저수준 함수)을 허용하게 되면 블록킹 호출되는 부분을 검출하기 힘들어짐
		 * reactor.blockhound.BlockingOperationError: Blocking call! java.io.RandomAccessFile#readBytes
		 * 	at java.base/java.io.RandomAccessFile.readBytes(RandomAccessFile.java)
		 * 	Suppressed: The stacktrace has been enhanced by Reactor, refer to additional information below:
		 * Error has been observed at the following site(s):
		 * 	*__checkpoint ⇢ Handler com.hello.webfluxstart.controller.HomeController#home() [DispatcherHandler]
		 * 	*__checkpoint ⇢ org.springframework.boot.web.reactive.filter.OrderedHiddenHttpMethodFilter [DefaultWebFilterChain]
		 * 	*__checkpoint ⇢ HTTP GET "/" [ExceptionHandlingWebHandler]
		 * Original Stack Trace:
		 * 		at java.base/java.io.RandomAccessFile.readBytes(RandomAccessFile.java)
		 * 		at java.base/java.io.RandomAccessFile.read(RandomAccessFile.java:406)
		 * 		at java.base/java.io.RandomAccessFile.readFully(RandomAccessFile.java:470)
		 * 		at java.base/java.util.zip.ZipFile$Source.readFullyAt(ZipFile.java:1304)
		 * 		at java.base/java.util.zip.ZipFile$ZipFileInputStream.initDataOffset(ZipFile.java:998)
		 * 		at java.base/java.util.zip.ZipFile$ZipFileInputStream.read(ZipFile.java:1013)
		 * 		at java.base/java.util.zip.ZipFile$ZipFileInflaterInputStream.fill(ZipFile.java:468)
		 * 		at java.base/java.util.zip.InflaterInputStream.read(InflaterInputStream.java:159)
		 * 		at java.base/java.io.InputStream.readNBytes(InputStream.java:490)
		 * 		at java.base/java.util.jar.JarFile.getBytes(JarFile.java:807)
		 * 		at java.base/java.util.jar.JarFile.checkForSpecialAttributes(JarFile.java:1007)
		 * 		at java.base/java.util.jar.JarFile.isMultiRelease(JarFile.java:390)
		 * 		at java.base/java.util.jar.JarFile.getEntry(JarFile.java:509)
		 * 		at java.base/sun.net.www.protocol.jar.URLJarFile.getEntry(URLJarFile.java:131)
		 * 		at java.base/sun.net.www.protocol.jar.JarURLConnection.connect(JarURLConnection.java:137)
		 * 		at java.base/sun.net.www.protocol.jar.JarURLConnection.getInputStream(JarURLConnection.java:155)
		 * 		at java.base/java.net.URLClassLoader.getResourceAsStream(URLClassLoader.java:328)
		 * 		at org.thymeleaf.util.ClassLoaderUtils.findResourceAsStream(ClassLoaderUtils.java:355)
		 * 		at org.thymeleaf.util.ClassLoaderUtils.loadResourceAsStream(ClassLoaderUtils.java:324)
		 * 		at org.thymeleaf.Thymeleaf.<clinit>(Thymeleaf.java:77)
		 * 		at org.thymeleaf.ConfigurationPrinterHelper.printConfiguration(ConfigurationPrinterHelper.java:88)
		 * 		at org.thymeleaf.TemplateEngine.initialize(TemplateEngine.java:346) ========================> 내부에서 RandomAccessFile#readBytes 라는 블록킹 함수를 호출하도록 되어있음
		 * 		at org.thymeleaf.TemplateEngine.getConfiguration(TemplateEngine.java:406)
		 * 		at org.thymeleaf.spring5.view.reactive.ThymeleafReactiveView.render(ThymeleafReactiveView.java:334)
		 * 		at org.springframework.web.reactive.result.view.ViewResolutionResultHandler.renderWith(ViewResolutionResultHandler.java:353)
		 * 		at org.springframework.web.reactive.result.view.ViewResolutionResultHandler.render(ViewResolutionResultHandler.java:341)
		 * 		at org.springframework.web.reactive.result.view.ViewResolutionResultHandler.lambda$null$0(ViewResolutionResultHandler.java:248)
		 */
		// BlockHound.install(); // 블록 하운드 등록 => 심은 후에 동작하지 않도록 제거하는 것은 불가능
		SpringApplication.run(WebfluxStartApplication.class, args);
	}

}
