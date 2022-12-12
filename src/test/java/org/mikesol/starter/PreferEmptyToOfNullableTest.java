package org.mikesol.starter;

import org.junit.jupiter.api.Test;
import org.openrewrite.java.JavaParser;
import org.openrewrite.test.RecipeSpec;
import org.openrewrite.test.RewriteTest;

import static org.openrewrite.java.Assertions.java;

public class PreferEmptyToOfNullableTest implements RewriteTest {
    @Override
    public void defaults(RecipeSpec spec) {
        spec
          .recipe(new PreferEmptyToOfNullable())
          .parser(JavaParser.fromJavaVersion()
            .logCompilationWarningsAndErrors(true));
    }

    @Test
    void nullInputShouldBeReplacedWithEmpty() {
        rewriteRun(
          java(
            """
                  import java.util.Optional;

                  class Test {
                      Optional emptyOptional = Optional.ofNullable(null);
                  }
              """,
            """
                  import java.util.Optional;

                  class Test {
                      Optional emptyOptional = Optional.empty();
                  }
              """
          )
        );
    }

    @Test
    void nonNullInputShouldNotBeReplaced() {
        rewriteRun(
          java(
            """
                  import java.util.Optional;

                  class Test {
                      Optional emptyOptional = Optional.ofNullable(123);
                  }
              """
          )
        );
    }
}
