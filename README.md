# Discord Reward Plugin

Discord Reward Plugin to plugin do Minecrafta, który nagradza graczy za dołączenie na serwer Discord. Gracz, po kliknięciu przycisku na Discordzie, wpisuje kod uzyskany w grze, aby odebrać nagrodę na serwerze Minecraft.

## Funkcje
- **Nagrody za dołączenie na Discord**: Gracze mogą odebrać nagrody po połączeniu konta Discord z Minecraft.
- **Generowanie kodów**: Każdy gracz otrzymuje unikalny kod, który musi wpisać na serwerze Discord, aby odebrać nagrodę.
- **Konfiguracja nagród**: W pełni konfigurowalne przedmioty oraz komendy, które są przyznawane graczowi po poprawnym wpisaniu kodu.
- **Przeładowanie konfiguracji**: Plugin obsługuje przeładowanie ustawień bez konieczności restartowania serwera.

## Użycie Komend

- `/odbierz`  
  **Opis:** Generuje kod, który gracz musi przepisać na Discordzie w celu odebrania nagrody.  
  **Przykład:** `/odbierz` – Wyświetla kod do przepisania na serwerze Discord.

## Konfiguracja

Plugin automatycznie tworzy pliki konfiguracyjne, w których można dostosować:

- **Nagrody**: Możesz ustawić przedmioty i komendy, które będą przyznawane po poprawnym wpisaniu kodu.
- **Wiadomości**: Ustaw wiadomości wyświetlane graczom, kiedy otrzymują nagrodę lub gdy wystąpi błąd.
- **Discord**: Konfiguracja tokenu bota oraz wiadomości na serwerze Discord.

### Przykład konfiguracji (`config.yml`):

```yaml
# BOT #

bot:
  token: "discord-bot-token"
  allowed_role: "access-role-id"
  activity:
    type: "WATCHING"
    name: "M4CODE.PL"

# DATABASE #

database:
  host: "localhost"
  port: "3306"
  name: "minecraft"
  user: "root"
  password: "password"

# ITEMKI #
items:
  material: "DIAMOND"
  amount: 1
  name: "&aNagroda VIP"
  lore:
  - "Specjalna nagroda za dołączenie na Discord"
  flags: []
  custom_model_data: 0
  enchantments:
    - name: UNBREAKING
      level: 3

# KOMENDY #
commands:
  - "lp user %player% parent set vip"
  - "say Nadano range VIP"
```

### Przykład wiadomości (`messages.yml`):

```yaml
# ODBIERZ #

odbierz:
  error: "&8» &9Odebrałeś już swoją nagrodę!"
  code_message1: "&8» &9Oto twój kod, przepisz go na kanale #odbierz na discordzie."
  code_message2: "&8» &9 {kod}"
  code_title: "&8» &9 {kod}"
  success: "&8» &aPomyślnie odebrano nagrodę"

# EMBED #

embed:
  title: "M4CODE.PL・ŁĄCZENIE KONTA"
  color: "#6d08d5"
  description: "Aby połączyć konto wykonaj poniższe kroki:\n\n1. Połącz się na serwer przez adres **m4code.pl**\n2. Wpisz komendę /odbierz\n3. Naciśnij przycisk **Połącz**"
  footer: "© 2024 M4CODE.PL"
  label: "Połącz"
  style: "secondary"

# DISCORD #
discord:
  error-dc: "To konto Discord już odebrało nagrodę"
  error: "Już odebrałeś swoją nagrodę!"
  success: "Odebrałeś nagrodę {nick}"
```

**Wymagania:**
- Serwer Minecraft (wersja 1.16+)
- Java 8+

**Autor:** [ScreamMaster1337](https://github.com/ScreamMaster1337)  
**Licencja:** MIT
