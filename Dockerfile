#Esto para construir
FROM gradle:8.5-jdk21 AS build
WORKDIR /app
#Para copiar código del proyecto al contenedor
COPY . .
#Para crear el .jar
RUN gradle build --no-daemon


#Aquí para ejecutarlo
FROM eclipse-temurin:21-jre
WORKDIR /app
#Se copia el Jar creado en el paso anterior
COPY --from=build /app/build/libs/*.jar app.jar
#El puerto ddonde se pretende correr SpringBoot
EXPOSE 8080
#El comando para activar
ENTRYPOINT ["java", "-jar", "app.jar"]
