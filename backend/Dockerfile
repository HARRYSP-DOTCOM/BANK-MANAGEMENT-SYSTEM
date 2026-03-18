FROM eclipse-temurin:21
WORKDIR /app
COPY . .
RUN javac -d . backend/Server.java
EXPOSE 8081
CMD ["java", "-cp", ".", "backend.Server"]
git add .