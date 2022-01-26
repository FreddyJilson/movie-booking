FROM mcr.microsoft.com/dotnet/sdk:5.0 AS build
WORKDIR /app
COPY ./frontend/*.csproj ./
RUN dotnet restore
COPY ./frontend ./
RUN dotnet publish -c Release

FROM nginx:alpine
RUN apk update
COPY ./frontend/nginx.conf /etc/nginx/nginx.conf
COPY --from=build /app/bin/Release/net5.0/publish/wwwroot /usr/share/nginx/html
EXPOSE 80