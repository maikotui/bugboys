# Bugboys Mod

**Bugboys** is a Fabric 1.20.1 Minecraft mod that introduces an RPG-style progression system built on top of the [SpellEngine](https://modrinth.com/mod/spell-engine), [Wizards](https://modrinth.com/mod/wizards), and [Revelationary](https://modrinth.com/mod/revelationary) mods.

This mod integrates with **Pufferfish Skills** and **Revelationary** to gate spellbook access behind skill unlocks and custom advancements. Players must progress through skill trees and earn specific achievements to reveal and use powerful magical items.

## Features

- 🔒 Skill-based spellbook access using Pufferfish Skills + Revelationary
- 🪄 Spell book locking with Revelationary’s cloaking system
- 📖 Integration with SpellEngine’s SpellBookTrinketItem
- 🚫 Prevents cloaked items from being equipped or used
- ✅ Built-in DataGen for custom advancements

## Development

### Dependencies

- Minecraft `1.20.1`
- Fabric Loader + API
- [SpellEngine](https://modrinth.com/mod/spell-engine)
- [Wizards](https://modrinth.com/mod/wizards)
- [Revelationary](https://modrinth.com/mod/revelationary)
- [Pufferfish Skills](https://modrinth.com/mod/skills)
- Other supporting mods (Runes, Spell Power, Cloth Config, etc.)

### Building

Clone the repo and run:

```bash
./gradlew build