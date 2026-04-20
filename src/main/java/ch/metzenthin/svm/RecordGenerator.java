package ch.metzenthin.svm;

import ch.metzenthin.svm.domain.model.conversion.BigDecimalConverter;
import ch.metzenthin.svm.domain.model.conversion.BooleanConverter;
import ch.metzenthin.svm.domain.model.conversion.CalendarConverter;
import ch.metzenthin.svm.domain.model.conversion.ConversionResult;
import ch.metzenthin.svm.domain.model.conversion.ConvertedFieldsAndConversionResults;
import ch.metzenthin.svm.domain.model.conversion.IntegerConverter;
import ch.metzenthin.svm.domain.model.conversion.TimeConverter;
import ch.metzenthin.svm.persistence.entities.AbstractEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

/**
 * @author Hans Stamm
 */
public class RecordGenerator {
  private static final String ENTITY_PACKAGE = "ch.metzenthin.svm.persistence.entities";
  private static final String TARGET_PACKAGE = "ch.metzenthin.svm.domain.model.entityfields";
  private static final String OUTPUT_DIR = "src/main/java/" + TARGET_PACKAGE;
  private static final String RECORD_NAME_PREFIX = "";
  private static final String RECORD_NAME_PREFIX_CONVERTED = "Converted";
  private static final String RECORD_NAME_SUFFIX = "Fields";

  public static void main(String[] args) throws Exception {
    List<Class<?>> classes = findClasses(ENTITY_PACKAGE);

    for (Class<?> clazz : classes) {
      if (clazz.isAnnotationPresent(Entity.class)) {
        generateRecord(clazz, RECORD_NAME_PREFIX_CONVERTED, false);
        generateRecord(clazz, RECORD_NAME_PREFIX, true);
      }
    }
  }

  // Package scannen (ohne externe Libs)
  private static List<Class<?>> findClasses(
      @SuppressWarnings("SameParameterValue") String packageName) throws Exception {
    String path = packageName.replace('.', '/');

    ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
    URL resource = classLoader.getResource(path);

    if (resource == null) {
      throw new IOException("Package not found: " + packageName);
    }

    File directory = new File(resource.toURI());

    List<Class<?>> classes = new ArrayList<>();
    findClassesInDirectory(packageName, directory, classes);

    return classes;
  }

  private static void findClassesInDirectory(
      String packageName, File directory, List<Class<?>> classes) throws Exception {
    if (!directory.exists()) return;

    for (File file : Objects.requireNonNull(directory.listFiles())) {
      if (file.isDirectory()) {
        findClassesInDirectory(packageName + "." + file.getName(), file, classes);
      } else if (file.getName().endsWith(".class")) {
        String className = packageName + "." + file.getName().replace(".class", "");
        classes.add(Class.forName(className));
      }
    }
  }

  // Record generieren
  private static void generateRecord(
      Class<?> entityClass, String recordNamePrefix, boolean convertEntityTypesToString)
      throws IOException {
    String recordName = recordNamePrefix + entityClass.getSimpleName() + RECORD_NAME_SUFFIX;

    List<Field> allFieldsInHierarchy = getAllFieldsInHierarchy(entityClass);
    boolean classContainsNoFieldsToBeConverted =
        allFieldsInHierarchy.stream().noneMatch(f -> hasFieldToBeConverted(f.getType()));
    if (classContainsNoFieldsToBeConverted && !convertEntityTypesToString) {
      // Kein ConvertedFields-Record für Klassen, die keine Fields enthalten, die konvertiert werden
      // müssen
      return;
    }

    Set<String> imports = new TreeSet<>();
    String components = buildComponents(allFieldsInHierarchy, imports, convertEntityTypesToString);

    if (components.isEmpty()) {
      return;
    }

    StringBuilder code = new StringBuilder();

    code.append("package ").append(TARGET_PACKAGE).append(";\n\n");

    String mapperMethods =
        generateMapper(
            entityClass,
            allFieldsInHierarchy,
            recordNamePrefix,
            imports,
            classContainsNoFieldsToBeConverted,
            convertEntityTypesToString);

    // Imports einfügen
    for (String imp : imports) {
      code.append("import ").append(imp).append(";\n");
    }

    if (!imports.isEmpty()) {
      code.append("\n");
    }

    code.append("/**\n * Dieser Record wurde generiert mit <")
        .append(RecordGenerator.class.getName())
        .append(">. Bitte keine manuellen Anpassungen!\n */\n")
        .append("@SuppressWarnings({\"unused\", \"DuplicatedCode\"})\n")
        .append("public record ")
        .append(recordName)
        .append("(\n  ")
        .append(components)
        .append(")");

    if (!mapperMethods.isEmpty()) {
      code.append(" {\n\n");
      code.append(mapperMethods);
      code.append("}\n");
    } else {
      code.append(" {}\n");
    }

    writeToFile(recordName, code.toString());
  }

  private static List<Field> getAllFieldsInHierarchy(Class<?> clazz) {
    Field[] declaredFields = clazz.getDeclaredFields();
    List<Field> filteredFields = new ArrayList<>(filterFields(declaredFields));
    Class<?> superclass = clazz.getSuperclass();
    if (superclass != null && !superclass.equals(AbstractEntity.class)) {
      filteredFields.addAll(getAllFieldsInHierarchy(superclass));
    }
    return filteredFields;
  }

  private static List<Field> filterFields(Field[] fields) {
    return Arrays.stream(fields)
        .filter(f -> !Modifier.isStatic(f.getModifiers()))
        .filter(f -> f.isAnnotationPresent(Column.class) && !f.isAnnotationPresent(Id.class))
        .filter(f -> !f.isAnnotationPresent(OneToOne.class))
        .toList();
  }

  private static String buildComponents(
      List<Field> fields, Set<String> imports, boolean convertEntityTypesToString) {
    return fields.stream()
        .map(
            f ->
                resolveType(f.getGenericType(), imports, convertEntityTypesToString)
                    + " "
                    + f.getName())
        .collect(Collectors.joining(",\n  "));
  }

  // Typ + Imports auflösen (inkl. Generics)
  private static String resolveType(
      Type type, Set<String> imports, boolean convertEntityTypesToString) {
    if (type instanceof Class<?> clazz) {

      if (clazz.isArray()) {
        return resolveType(clazz.getComponentType(), imports, convertEntityTypesToString) + "[]";
      }

      Class<?> resolvedClazz = clazz;
      if (hasFieldToBeConvertedToString(convertEntityTypesToString, clazz)) {
        resolvedClazz = String.class;
      }

      addImport(resolvedClazz, imports);
      return resolvedClazz.getSimpleName();
    }

    if (type instanceof ParameterizedType pt) {
      Class<?> rawType = (Class<?>) pt.getRawType();
      addImport(rawType, imports);

      String generics =
          Arrays.stream(pt.getActualTypeArguments())
              .map(t -> resolveType(t, imports, convertEntityTypesToString))
              .collect(Collectors.joining(", "));

      return rawType.getSimpleName() + "<" + generics + ">";
    }

    return type.getTypeName(); // Fallback
  }

  private static boolean hasFieldToBeConvertedToString(
      boolean convertEntityTypesToString, Class<?> fieldClazz) {
    if (!convertEntityTypesToString) {
      return false;
    }
    return hasFieldToBeConverted(fieldClazz);
  }

  private static boolean hasFieldToBeConverted(Class<?> fieldClazz) {
    return !fieldClazz.getTypeName().equals("boolean")
        && !fieldClazz.isEnum()
        && !fieldClazz.isAssignableFrom(String.class);
  }

  private static void addImport(Class<?> clazz, Set<String> imports) {
    if (clazz.isPrimitive()) return;

    String packageName = clazz.getPackageName();

    if (packageName.equals("java.lang")) return;
    if (packageName.equals(TARGET_PACKAGE)) return;

    imports.add(clazz.getName());
  }

  private static void writeToFile(String className, String content) throws IOException {
    File dir = new File(OUTPUT_DIR.replace('.', '/'));
    if (!dir.exists()) {
      // noinspection ResultOfMethodCallIgnored
      dir.mkdirs();
    }

    File file = new File(dir, className + ".java");

    try (FileWriter writer = new FileWriter(file)) {
      writer.write(content);
    }
  }

  private static String generateMapper(
      Class<?> entityClass,
      List<Field> fields,
      String recordNamePrefix,
      Set<String> imports,
      boolean classContainsNoFieldsToBeConverted,
      boolean convertEntityTypesToString) {
    String entityName = entityClass.getSimpleName();
    String recordName = recordNamePrefix + entityName + RECORD_NAME_SUFFIX;

    imports.add(entityClass.getName());

    StringBuilder code = new StringBuilder();

    // of
    code.append("  public static ")
        .append(recordName)
        .append(" of(")
        .append(entityName)
        .append(" entity) {\n");

    code.append("    if (entity == null) return null;\n\n");

    code.append("    return new ").append(recordName).append("(\n");

    code.append(
        fields.stream()
            .map(f -> "      " + getMappingStatement(convertEntityTypesToString, f, imports))
            .collect(Collectors.joining(",\n")));

    code.append("\n    );\n");
    String endOfMethod = "  }\n";
    code.append(endOfMethod);

    if (classContainsNoFieldsToBeConverted || !convertEntityTypesToString) {
      // mergeIntoEntity
      code.append("\n  public void mergeIntoEntity(").append(entityName).append(" entity) {\n");

      code.append("    if (entity == null) return;\n\n");

      fields.forEach(
          f ->
              code.append("    entity.")
                  .append(setterName(f))
                  .append("(")
                  .append(f.getName())
                  .append("());\n"));

      code.append(endOfMethod);
    }

    if (!classContainsNoFieldsToBeConverted && convertEntityTypesToString) {
      // convert
      String convertedRecordName = RECORD_NAME_PREFIX_CONVERTED + recordName;
      code.append("\n  @SuppressWarnings(\"java:S3776\")\n")
          .append("  public ")
          .append("ConvertedFieldsAndConversionResults<")
          .append(convertedRecordName)
          .append("> convert() {\n")
          .append("    List<ConversionResult<?>> conversionErrors = new ArrayList<>();\n\n");
      addImport(ConvertedFieldsAndConversionResults.class, imports);
      addImport(ConversionResult.class, imports);
      addImport(List.class, imports);
      addImport(ArrayList.class, imports);

      String conversionStatements =
          buildConversionStatementsAndReturnStatement(convertedRecordName, fields, imports);
      if (!conversionStatements.isEmpty()) {
        code.append(conversionStatements);
      }

      code.append(endOfMethod);
    }

    return code.toString();
  }

  private static String buildConversionStatementsAndReturnStatement(
      String convertedRecordName, List<Field> fields, Set<String> imports) {

    List<String> conversionStatements = new ArrayList<>();
    List<String> fieldGetters = new ArrayList<>();
    fields.forEach(f -> buildConversionStatements(f, conversionStatements, fieldGetters, imports));

    String convertedRecordNameUncapitalized = uncapitalize(convertedRecordName);
    return String.join("\n", conversionStatements)
        + ((!conversionStatements.isEmpty()) ? "\n\n    " : "    ")
        + convertedRecordName
        + " "
        + convertedRecordNameUncapitalized
        + " = new "
        + convertedRecordName
        + "(\n      "
        + String.join(",\n      ", fieldGetters)
        + "\n    );\n\n"
        + "    return new ConvertedFieldsAndConversionResults<>("
        + convertedRecordNameUncapitalized
        + ", conversionErrors);\n";
  }

  private static void buildConversionStatements(
      Field field,
      List<String> conversionStatements,
      List<String> fieldGetters,
      Set<String> imports) {
    if (hasFieldToBeConvertedToString(true, field.getType())) {
      conversionStatements.add(getConvertToStatements(field, imports));
      fieldGetters.add(field.getName() + ".convertedValue()");
    } else {
      fieldGetters.add(field.getName() + "()");
    }
  }

  /**
   * Beispiel:<br>
   * ConversionResult<Calendar> semesterBeginn =
   * CalendarConverter.convertToCalendar("semesterbeginn", semesterbeginn());<br>
   * if (semesterbeginn.isValid()) conversionErrors.add(semesterbeginn);
   */
  private static String getConvertToStatements(Field f, Set<String> imports) {
    StringBuilder convertToStatement = new StringBuilder();
    String fieldName = f.getName();
    String getterName = fieldName + "()";
    convertToStatement
        .append("    ConversionResult<")
        .append(getTypeName(f))
        .append("> ")
        .append(fieldName)
        .append(" = ");
    addImport(f.getType(), imports);
    switch (f.getType().getTypeName()) {
      case "java.math.BigDecimal" -> {
        addImport(BigDecimalConverter.class, imports);
        convertToStatement.append("BigDecimalConverter.convertToBigDecimal");
      }
      case "java.lang.Boolean" -> {
        addImport(BooleanConverter.class, imports);
        convertToStatement.append("BooleanConverter.convertToBoolean");
      }
      case "java.util.Calendar" -> {
        addImport(CalendarConverter.class, imports);
        convertToStatement.append("CalendarConverter.convertToCalendar");
      }
      case "int", "java.lang.Integer" -> {
        addImport(IntegerConverter.class, imports);
        convertToStatement.append("IntegerConverter.convertToInteger");
      }
      case "java.sql.Time" -> {
        addImport(TimeConverter.class, imports);
        convertToStatement.append("TimeConverter.convertToTime");
      }
      default -> throw new IllegalStateException("Unexpected value: " + f.getType().getTypeName());
    }
    convertToStatement
        .append("(\"")
        .append(fieldName)
        .append("\", ")
        .append(getterName)
        .append(");\n")
        .append("    if (!")
        .append(fieldName)
        .append(".isValid()) conversionErrors.add(")
        .append(fieldName)
        .append(");");

    return convertToStatement.toString();
  }

  private static String getTypeName(Field f) {
    if (f.getType().isPrimitive() && int.class.equals(f.getType())) {
      return Integer.class.getSimpleName();
    }
    return f.getType().getSimpleName();
  }

  private static String getMappingStatement(
      boolean convertEntityTypesToString, Field f, Set<String> imports) {
    String getter = "entity." + getterName(f) + "()";
    if (!convertEntityTypesToString || !hasFieldToBeConvertedToString(true, f.getType())) {
      return getter;
    }
    String mapping;
    switch (f.getType().getTypeName()) {
      case "java.math.BigDecimal" -> {
        addImport(BigDecimalConverter.class, imports);
        mapping = "BigDecimalConverter.toString(" + getter + ")";
      }
      case "java.lang.Boolean" -> {
        addImport(BooleanConverter.class, imports);
        mapping = "BooleanConverter.toString(" + getter + ")";
      }
      case "java.util.Calendar" -> {
        addImport(CalendarConverter.class, imports);
        mapping = "CalendarConverter.toString(" + getter + ")";
      }
      case "int", "java.lang.Integer" -> {
        addImport(IntegerConverter.class, imports);
        mapping = "IntegerConverter.toString(" + getter + ")";
      }
      case "java.sql.Time" -> {
        addImport(TimeConverter.class, imports);
        mapping = "TimeConverter.toString(" + getter + ")";
      }
      default -> throw new IllegalStateException("Unexpected value: " + f.getType().getTypeName());
    }
    return mapping;
  }

  private static String getterName(Field f) {
    String name = f.getName();
    String prefix = f.getType().equals(boolean.class) ? "is" : "get";
    return prefix + capitalize(name);
  }

  private static String setterName(Field f) {
    return "set" + capitalize(f.getName());
  }

  private static String capitalize(String s) {
    return s.substring(0, 1).toUpperCase() + s.substring(1);
  }

  private static String uncapitalize(String s) {
    return s.substring(0, 1).toLowerCase() + s.substring(1);
  }
}
