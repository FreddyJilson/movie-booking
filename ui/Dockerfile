FROM mcr.microsoft.com/dotnet/sdk:5.0 AS build
WORKDIR /app
COPY ./ui/*.csproj ./
RUN dotnet restore
COPY ./ui ./
RUN dotnet publish -c Release

FROM nginx:alpine
RUN apk update
COPY ./ui/nginx.conf /etc/nginx/nginx.conf
COPY --from=build /app/bin/Release/net5.0/publish/wwwroot /usr/share/nginx/html
EXPOSE 80