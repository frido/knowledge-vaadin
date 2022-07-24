package com.example.application;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import com.google.common.truth.Truth;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;

/**
 * Vyrobim si class loader URLClassLoader.
 * Automaticky sa mu nastavi applikacny class loader ako parent.
 * Ked loadne classu, najskor sa opyta parenta aby vratil classu.
 * Kedze applikacny class loader ju ma u seba preto ju vrati -
 * preto ma ta instancia nastaveny class loader applikacny class loader,
 * aj ked je vyrobena mojim class loaderom.
 *
 */
public class ClassLoaderTest {

    public String getNameToAssert() {
        return "XXX";
    }

    @SneakyThrows
    @Test
    public void name() throws ClassNotFoundException {
        Set<Path> paths = Collections.singleton(Paths.get("src", "main", "java"));
        List<URL> uris = paths.stream()
            .map(Path::toUri)
            .map(this::toURL)
            .collect(Collectors.toList());

        ClassLoader myLoader = URLClassLoader.newInstance(uris.toArray(new URL[0]));
        Class<?> myClass = myLoader.loadClass("com.example.application.ClassLoaderTest");

        Class<ClassLoaderTest> appClass = ClassLoaderTest.class;
        ClassLoader appLoader = appClass.getClassLoader();

        ClassLoaderTest newInstance = (ClassLoaderTest)myClass.getConstructor().newInstance();

        // parent mojho class loadera je applikacny loader
        Truth.assertThat(myLoader.getParent()).isEqualTo(appLoader);

        // myLoader sa opyta na applikacny loader aby vratil classu a on ju aj naozaj vrati
        Truth.assertThat(myClass).isEqualTo(appClass);

        // preto ma instancia nastaveny applikacny loader
        Truth.assertThat(appLoader).isEqualTo(newInstance.getClass().getClassLoader());

        // kontrola ze instancia naozaj funguje
        Truth.assertThat(newInstance.getNameToAssert()).matches("XXX");

        ClassLoader currentLoader = myLoader;
        while (currentLoader != null) {
            System.out.println(currentLoader);
            currentLoader = currentLoader.getParent();
        }
    }

    private URL toURL(URI uri) {
        URL url = null;
        try {
            url = uri.toURL();
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
        return url;
    }
}
