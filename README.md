# Scripts to interact with the TP-LINK router AX1800

# Credentials:
1. Create a strong password for the admin and store it in the secret manager. After you hit enter, a password will be asked.
```bash
    secret-tool store --label="TP-Link-AX20" password "TP-Link-AX20"
```    

2. Retrieve the admin's password.
```bash
    secret-tool lookup password "TP-Link-AX20"
```

# Install java and maven
Run the command in the terminal:
```bash
  sudo apt install openjdk-18-jre-headless -y
  sudo apt install maven -y
```

# Compile the application
Run the command in the terminal:
```bash
  cd git/datacenter/projects/tplink_router_ax1800
  mvn package
```

# Run the application
Run the command in the terminal:
```bash
  java -cp target/tplink.router.ax1800-0.0.1-SNAPSHOT-jar-with-dependencies.jar tplink.router.ax1800.Setup
  java -jar tplink.router.ax1800.Setup
  or 
  java -jar target/tplink.router.ax1800-0.0.1-SNAPSHOT-jar-with-dependencies.jar
```

# License:

[MIT](LICENSE "MIT License")

# Created by: 

1. Luciano Sampaio.
