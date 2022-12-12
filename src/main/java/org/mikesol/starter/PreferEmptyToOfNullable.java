package org.mikesol.starter;

import org.openrewrite.ExecutionContext;
import org.openrewrite.Recipe;
import org.openrewrite.TreeVisitor;
import org.openrewrite.java.JavaTemplate;
import org.openrewrite.java.JavaVisitor;
import org.openrewrite.java.MethodMatcher;
import org.openrewrite.java.tree.J;

public class PreferEmptyToOfNullable extends Recipe {
    // Unsure why I have to use .. here instead of *. Maybe a bug with MethodMatcher?
    private static final MethodMatcher OF_NULLABLE_NULL = new MethodMatcher("java.util.Optional ofNullable(..)");

    @Override
    public String getDisplayName() {
        return "Use `Optional.empty()` instead of `Optional.ofNullable(null)`";
    }

    @Override
    public String getDescription() {
        return "Prefer the more readable and clear `empty()` when creating an empty Optional.";
    }

    @Override
    protected TreeVisitor<?, ExecutionContext> getVisitor() {
        return new JavaVisitor<ExecutionContext>() {
            private final JavaTemplate emptyOptional = JavaTemplate.builder(this::getCursor, "Optional.empty()")
                    .build();

            @Override
            public J visitMethodInvocation(J.MethodInvocation method, ExecutionContext executionContext) {

                if (OF_NULLABLE_NULL.matches(method)) {
                    if (method.getArguments().get(0) instanceof J.Literal) {
                        J.Literal firstArg = (J.Literal) method.getArguments().get(0);

                        if (firstArg.getValue() == null) {
                            return method.withTemplate(emptyOptional, method.getCoordinates().replace());
                        }
                    }
                }

                return method;
            }
        };
    }
}
