package de.consol.dus.s4.services.rest.api.arch;

import static com.tngtech.archunit.library.Architectures.layeredArchitecture;

import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;

@AnalyzeClasses(packages = {
    "de.consol.dus.s4.services.rest.api",
    "javax.persistence",
    "org.springframework.data.jpa",
    "javax.validation",
    "javax.ws",
    "org.eclipse.microprofile.openapi"})
public class AccessStructureTest {

  @ArchTest
  public static final ArchRule architectureIsSane = layeredArchitecture()
      .layer("useCases").definedBy("..usecases")
      .layer("useCases.api").definedBy("..usecases.api..")
      .layer("useCases.spi").definedBy("..usecases.spi..")
      .layer("useCases.internal.api").definedBy("..usecases.internal.api..")
      .layer("useCases.spi.dao").definedBy("..usecases.spi.dao..")
      .layer("rest").definedBy("..boundary.rest..")
      .layer("dao").definedBy("..boundary.dao..")
      .layer("quarkus").definedBy("..boundary.quarkus..")
      .layer("persistence").definedBy("javax.persistence..", "org.springframework.data.jpa..")
      .layer("web").definedBy("javax.validation..", "org.eclipse.microprofile.openapi..")

      .whereLayer("useCases")
          .mayOnlyBeAccessedByLayers("useCases.api", "useCases.internal.api", "quarkus")
      .whereLayer("useCases.spi.dao").mayOnlyBeAccessedByLayers(
          "useCases",
          "useCases.api",
          "useCases.internal.api",
          "dao",
          "quarkus")
      .whereLayer("dao").mayOnlyBeAccessedByLayers("quarkus")
      .whereLayer("rest").mayOnlyBeAccessedByLayers("quarkus")
      .whereLayer("quarkus").mayNotBeAccessedByAnyLayer()
      .whereLayer("persistence").mayOnlyBeAccessedByLayers("dao")
      .whereLayer("web").mayOnlyBeAccessedByLayers("rest");
}
