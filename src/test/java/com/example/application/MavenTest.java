package com.example.application;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.stream.Stream;
import org.junit.Test;

/**
 * How to define maven build process
 */
public class MavenTest {
    
    private static final String PROFILE_SOURCE = "source: frido.petrzalka:vaadin:1.0-SNAPSHOT";

    /**
     * Maven has defined (hardcoded) phases for each lifecycle (jar, pom, site)
     * For example jar lifecycle has about 30 phases.
     * Plugin defines own goals that can be bind to the particular phase.
     * There are also predefined plugins with default binds of goals to the phase (Like compiling - we don't need to define it).
     * We can customize preddefined plugins by definind it in the pom.xml.
     * We can also define new plugins and define what goals we want to execute.
     * 
     * We define plugins in the build tag. Build tag can be in the root or in the profile tag.
     * 
     * lifestyle (jar) --define--> phases <--bind to (when to execute)-- goal <--define-- plugin
     * {@code
    <profiles>
        <profile>
            <id>junit-test</id>
            <build>
                <plugins>
                    <plugin>
                        <groupId>org.apache.maven.plugins</groupId>
                        <artifactId>maven-help-plugin</artifactId>
                        <version>3.2.0</version>
                        <executions>
                            <execution>
                                <id>show-profiles</id>
                                <phase>validate</phase>
                                <goals>
                                    <goal>active-profiles</goal>
                                </goals>
                            </execution>
                        </executions>
                    </plugin>
                </plugins>
            </build>
        </profile>
    <profiles> 
     }
     * 
     * @throws IOException
     */
    @Test
    public void build() throws IOException {
        // show-profiles is not executed without profile
        boolean activeProfileExecuted = exec("mvn.cmd validate").anyMatch(line -> line.contains(PROFILE_SOURCE));
        // show-profiles is executed for the junit-test profile
        boolean activeProfileExecutedWithProfile = exec("mvn.cmd validate -P junit-test").anyMatch(line -> line.contains(PROFILE_SOURCE));

        assertEquals(false, activeProfileExecuted);
        assertEquals(true, activeProfileExecutedWithProfile);
    }

    /**
     * dependencyManagement defines versions of dependencies. 
     * Can be inherited from parent or imported in scope import.
     */
    @Test
    public void dependencyManagement() {
        assertTrue(true);
    }

    private Stream<String> exec(String cmd) throws IOException {
        Process process = Runtime.getRuntime().exec(cmd);
        BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
        return reader.lines();
    }
}
