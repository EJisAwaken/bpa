FROM python:3.13.3-slim as dev

# Installer les outils nÃ©cessaires
RUN apt-get update && apt-get install -y \
    curl \
    gnupg \
    gcc \
    g++ \
    apt-transport-https \
    unixodbc \
    unixodbc-dev \
    ca-certificates

# âœ… Ajouter proprement la clÃ© Microsoft
RUN curl -sSL https://packages.microsoft.com/keys/microsoft.asc | gpg --dearmor > /usr/share/keyrings/microsoft.gpg

# âœ… Ajouter le dÃ©pÃ´t Microsoft de maniÃ¨re sÃ©curisÃ©e
RUN echo "deb [arch=amd64 signed-by=/usr/share/keyrings/microsoft.gpg] https://packages.microsoft.com/debian/12/prod bookworm main" > /etc/apt/sources.list.d/mssql-release.list

# âœ… Installer le driver SQL Server
RUN apt-get update && ACCEPT_EULA=Y apt-get install -y msodbcsql17

# Nettoyage
RUN apt-get clean && rm -rf /var/lib/apt/lists/*

# Configuration Django
WORKDIR /app
COPY . .
RUN pip install --no-cache-dir -r requirements.txt

EXPOSE 8000
CMD ["python", "manage.py", "runserver", "0.0.0.0:8000"]

# Stage 1: Builder
FROM python:3.13.3-slim as builder

WORKDIR /app

# Installer les dépendances système
RUN apt-get update && apt-get install -y \
    curl gnupg gcc g++ build-essential \
    unixodbc unixodbc-dev \
    apt-transport-https \
    ca-certificates \
    && rm -rf /var/lib/apt/lists/*

# Clé et dépôt Microsoft
RUN curl -sSL https://packages.microsoft.com/keys/microsoft.asc | gpg --dearmor > /usr/share/keyrings/microsoft.gpg && \
    echo "deb [arch=amd64 signed-by=/usr/share/keyrings/microsoft.gpg] https://packages.microsoft.com/debian/12/prod bookworm main" > /etc/apt/sources.list.d/mssql-release.list

# Installer MS ODBC SQL Server
RUN apt-get update && ACCEPT_EULA=Y apt-get install -y msodbcsql17 && rm -rf /var/lib/apt/lists/*

# Installer les dépendances Python dans un virtualenv
RUN python -m venv /opt/venv
ENV PATH="/opt/venv/bin:$PATH"

COPY requirements.txt ./
RUN pip install --no-cache-dir -U pip && \
    pip install --no-cache-dir -r requirements.txt

# Stage 2: Production
FROM python:3.13.3-slim as prod

ENV PYTHONDONTWRITEBYTECODE=1
ENV PYTHONUNBUFFERED=1
ENV PATH="/opt/venv/bin:$PATH"

WORKDIR /app

# Copier l'environnement virtuel depuis le builder
COPY --from=builder /opt/venv /opt/venv

# Installer les outils réseau utiles
RUN apt-get update && apt-get install -y \
    iputils-ping net-tools dnsutils ca-certificates \
    && rm -rf /var/lib/apt/lists/*

# Copier le code source
COPY . .

# Créer un dossier de logs
RUN mkdir -p logs

# Exposer le port
EXPOSE 8000

# Démarrer avec Gunicorn
CMD ["gunicorn", "--bind", "0.0.0.0:8000", "--workers=3", "--timeout=120", "--keep-alive=5", "config.wsgi:application"]
