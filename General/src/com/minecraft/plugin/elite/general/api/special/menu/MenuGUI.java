package com.minecraft.plugin.elite.general.api.special.menu;

import com.minecraft.plugin.elite.general.General;
import com.minecraft.plugin.elite.general.GeneralLanguage;
import com.minecraft.plugin.elite.general.api.Server;
import com.minecraft.plugin.elite.general.api.abstracts.GUI;
import com.minecraft.plugin.elite.general.api.ePlayer;
import com.minecraft.plugin.elite.general.api.enums.Achievement;
import com.minecraft.plugin.elite.general.api.enums.Language;
import com.minecraft.plugin.elite.general.api.enums.Prefix;
import com.minecraft.plugin.elite.general.api.enums.Rank;
import com.minecraft.plugin.elite.general.api.special.clan.Clan;
import com.minecraft.plugin.elite.general.database.Database;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.sql.ResultSet;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class MenuGUI extends GUI {

	public MenuGUI(Language lang) {
		super(lang);
	}

	private ItemStack back() {
		Server server = Server.get();
		ItemStack back = new ItemStack(Material.SUGAR);
		server.rename(back, this.getLanguage().get(GeneralLanguage.MENU_GUI_BACK));
		return back;
	}

	private ItemStack glass() {
		Server server = Server.get();
		ItemStack glass = new ItemStack(Material.STAINED_GLASS_PANE);
		server.rename(glass, " ");
		return glass;
	}

	public Inventory main() {
		this.build(45, GeneralLanguage.MENU_GUI_TITLE);

		Server server = Server.get();
		
		ItemStack staff = new ItemStack(Material.IRON_CHESTPLATE);
		server.rename(staff, this.getLanguage().get(GeneralLanguage.MENU_GUI_STAFF));
		
		ItemStack achievements = new ItemStack(Material.PAINTING);
		server.rename(achievements, this.getLanguage().get(GeneralLanguage.MENU_GUI_ACHIEVEMENTS));
		
		ItemStack faq = new ItemStack(Material.BOOK);
		server.rename(faq, this.getLanguage().get(GeneralLanguage.MENU_GUI_FAQ));
		
		ItemStack applications = new ItemStack(Material.BOOK_AND_QUILL);
		server.rename(applications, this.getLanguage().get(GeneralLanguage.MENU_GUI_APPLICATION));
		
		ItemStack stats = new ItemStack(Material.DIAMOND_SWORD);
		server.rename(stats, this.getLanguage().get(GeneralLanguage.MENU_GUI_STATS));
		
		ItemStack update = new ItemStack(Material.PAPER);
		server.rename(update, this.getLanguage().get(GeneralLanguage.MENU_GUI_UPDATE));

		this.fill(this.glass());
		this.getInventory().setItem(10, staff);
		this.getInventory().setItem(13, applications);
		this.getInventory().setItem(16, update);
		this.getInventory().setItem(28, achievements);
		this.getInventory().setItem(31, stats);
		this.getInventory().setItem(34, faq);
		return this.getInventory();
	}
	
	public Inventory application() {
		this.build(27, GeneralLanguage.MENU_GUI_APPLICATION);
		Server server = Server.get();
		
		ItemStack where = new ItemStack(Material.COMPASS);
		server.rename(where, this.getLanguage().get(GeneralLanguage.MENU_GUI_APPLICATION_APPLY));
		
		ItemStack mod = new ItemStack(Material.DIAMOND_SWORD);
		server.rename(mod, this.getLanguage().get(GeneralLanguage.MENU_GUI_APPLICATION_MOD));

		ItemStack supp = new ItemStack(Material.DIAMOND_AXE);
		server.rename(supp, this.getLanguage().get(GeneralLanguage.MENU_GUI_APPLICATION_SUPPORTER));
		
		ItemStack builder = new ItemStack(Material.DIAMOND_PICKAXE);
		server.rename(builder, this.getLanguage().get(GeneralLanguage.MENU_GUI_APPLICATION_BUILDER));

		this.fill(this.glass());
		this.getInventory().setItem(4, this.back());
		this.getInventory().setItem(10, mod);
		this.getInventory().setItem(11, supp);
		this.getInventory().setItem(13, where);
		this.getInventory().setItem(16, builder);
		return this.getInventory();
	}
	
	public Inventory stats(ePlayer p) {

		this.build(27, GeneralLanguage.MENU_GUI_STATS);
		Server server = Server.get();
    	
    	//load the xp bar
    	StringBuilder exp = new StringBuilder();
    	int amount = 0;
    	int formula = (int) ((20 * p.getExp()) / (p.getExp() + p.getRequiredExpForNextLevel(p.getLevel())));
    	for(int i = 0; i < formula; i++) {
    		exp.append(ChatColor.GOLD + "■");
    		amount++;
    	}
    	for(int i = 0; i < 20 - amount; i++)
    		exp.append(ChatColor.DARK_GRAY + "■");
    	
    	Clan clan = p.getClan();
    	ItemStack head = server.playerHead(p.getName());
		server.rename(head, this.getLanguage().get(GeneralLanguage.MENU_GUI_STATS_GENERAL)
				.replaceAll("%rank", p.getRank().getDisplayName())
				.replaceAll("%clan", (clan == null ? "" : clan.getName()))
				.replaceAll("%prestige", Integer.toString(p.getPrestige()))
				.replaceAll("%level", Integer.toString(p.getLevel()))
				.replaceAll("%exp", ChatColor.GOLD + Long.toString((((p.getLevel() + 1) * 100) - p.getRequiredExpForNextLevel(p.getLevel()))) + exp.toString() + ChatColor.DARK_GRAY + Long.toString(p.getRequiredExpForNextLevel(p.getLevel())))
				.replaceAll("%tokens", Long.toString(p.getTokens())));

    	DecimalFormat format = new DecimalFormat("0.00");
    	ItemStack pvp = new ItemStack(Material.DIAMOND_SWORD);
		server.rename(pvp, this.getLanguage().get(GeneralLanguage.MENU_GUI_STATS_ACTIVITY)
				.replaceAll("%kills", Long.toString(p.getKills()))
				.replaceAll("%deaths", Long.toString(p.getDeaths()))
				.replaceAll("%kdr", format.format(p.getKDR())));
		
		ItemStack time = new ItemStack(Material.WATCH);
		server.rename(time, this.getLanguage().get(GeneralLanguage.MENU_GUI_STATS_TIME)
				.replaceAll("%firstjoin", server.getDate(p.getFirstJoin()))
				.replaceAll("%lastjoin", server.getDate(p.getLastJoin()))
				.replaceAll("%playtime", server.getTime(p.getPlayTime(), p.getLanguage())));
		
		ItemStack prestige = new ItemStack(Material.BLAZE_POWDER);
		if(!p.isMasterPrestige() && p.getLevel() >= 55)
			server.rename(prestige, this.getLanguage().get(GeneralLanguage.MENU_GUI_STATS_PRESTIGE_SET));
		else
			server.rename(prestige, this.getLanguage().get(GeneralLanguage.MENU_GUI_STATS_PRESTIGE_LOCKED));

		this.fill(this.glass());
		this.getInventory().setItem(4, this.back());
		this.getInventory().setItem(11, head);
		this.getInventory().setItem(13, pvp);
		this.getInventory().setItem(15, time);
		this.getInventory().setItem(22, prestige);
		return this.getInventory();
	}
	
	public Inventory updateLog() {
		this.build(27, GeneralLanguage.MENU_GUI_UPDATE);
		Server server = Server.get();

		List<ItemStack> updates = new ArrayList<>();

		ItemStack update1 = new ItemStack(Material.PAPER);
		server.rename(update1, this.getLanguage().get(GeneralLanguage.MENU_GUI_UPDATE_1));
		updates.add(update1);

		ItemStack update2 = new ItemStack(Material.PAPER);
		server.rename(update2, this.getLanguage().get(GeneralLanguage.MENU_GUI_UPDATE_2));
		updates.add(update2);

		ItemStack update3 = new ItemStack(Material.PAPER);
		server.rename(update3, this.getLanguage().get(GeneralLanguage.MENU_GUI_UPDATE_3));
		updates.add(update3);

		ItemStack update4 = new ItemStack(Material.PAPER);
		server.rename(update4, this.getLanguage().get(GeneralLanguage.MENU_GUI_UPDATE_4));
		updates.add(update4);

		ItemStack update5 = new ItemStack(Material.PAPER);
		server.rename(update5, this.getLanguage().get(GeneralLanguage.MENU_GUI_UPDATE_5));
		updates.add(update5);

		ItemStack update6 = new ItemStack(Material.PAPER);
		server.rename(update6, this.getLanguage().get(GeneralLanguage.MENU_GUI_UPDATE_6));
		updates.add(update6);

		ItemStack update7 = new ItemStack(Material.PAPER);
		server.rename(update7, this.getLanguage().get(GeneralLanguage.MENU_GUI_UPDATE_7));
		updates.add(update7);

		this.fill(this.glass());
		this.getInventory().setItem(4, this.back());
		for(int i = 0; i < 7; i++)
			this.getInventory().setItem(i + 10, updates.get(i));
		return this.getInventory();
	}

	public Inventory achievements(ePlayer p, int page) {
		List<Achievement> achievements = new ArrayList<>();
		Collections.addAll(achievements, Achievement.values());
		this.buildPageType(GeneralLanguage.MENU_GUI_ACHIEVEMENTS, page, achievements.size(), this.glass());
		Server server = Server.get();

		for(int i = this.getLastSlot(page); i < this.getNextSlot(page); i++) {
			if(i >= achievements.size())
				break;
			Achievement achievement = achievements.get(i);
			ChatColor color;
			Material mat;
			if(p.hasAchievement(achievement)) {
				color = ChatColor.GREEN;
				mat = Material.GOLD_BLOCK;
			} else {
				color = ChatColor.RED;
				mat = Material.COAL_BLOCK;
			}
			ItemStack achStack = new ItemStack(mat);

			server.rename(achStack, color + achievement.getDisplay(this.getLanguage()));

			this.getInventory().addItem(achStack);
		}
		return this.getInventory();
	}
	
	public Inventory faqMain() {
		this.build(27, GeneralLanguage.MENU_GUI_FAQ);
		Server server = Server.get();
		
		ItemStack rules = new ItemStack(Material.BOOK, 4);
		server.rename(rules, this.getLanguage().get(GeneralLanguage.MENU_GUI_FAQ_RULES));
		
		ItemStack levels = new ItemStack(Material.BOOK, 7);
		server.rename(levels, this.getLanguage().get(GeneralLanguage.MENU_GUI_FAQ_LEVELS));
		
		ItemStack store = new ItemStack(Material.BOOK);
		server.rename(store, this.getLanguage().get(GeneralLanguage.MENU_GUI_FAQ_STORE));
		
		ItemStack ts = new ItemStack(Material.BOOK, 3);
		server.rename(ts, this.getLanguage().get(GeneralLanguage.MENU_GUI_FAQ_TEAMSPEAK));
		
		ItemStack unban = new ItemStack(Material.BOOK, 3);
		server.rename(unban, this.getLanguage().get(GeneralLanguage.MENU_GUI_FAQ_UNBAN));
		
		ItemStack contact = server.playerHead("MHF_QUESTION");
		server.rename(contact,this.getLanguage().get(GeneralLanguage.MENU_GUI_FAQ_SUPPORT));

		this.fill(this.glass());
		this.getInventory().setItem(4, this.back());
		this.getInventory().setItem(10, rules);
		this.getInventory().setItem(11, levels);
		this.getInventory().setItem(12, store);
		this.getInventory().setItem(13, ts);
		this.getInventory().setItem(14, unban);
		this.getInventory().setItem(15, contact);
		return this.getInventory();
	}

	public Inventory faqRules() {
		this.build(27, GeneralLanguage.MENU_GUI_FAQ_RULES);
		Server server = Server.get();

		ItemStack back = new ItemStack(Material.REDSTONE);
		server.rename(back, this.getLanguage().get(GeneralLanguage.MENU_GUI_BACK));

		ItemStack mods = new ItemStack(Material.PAPER);
		server.rename(mods, this.getLanguage().get(GeneralLanguage.MENU_GUI_FAQ_RULES_MODS));

		ItemStack chat = new ItemStack(Material.PAPER);
		server.rename(chat, this.getLanguage().get(GeneralLanguage.MENU_GUI_FAQ_RULES_CHAT));

		ItemStack team = new ItemStack(Material.PAPER);
		server.rename(team, this.getLanguage().get(GeneralLanguage.MENU_GUI_FAQ_RULES_TEAMS));

		ItemStack more = new ItemStack(Material.PAPER);
		server.rename(more, this.getLanguage().get(GeneralLanguage.MENU_GUI_FAQ_RULES_TERMS));

		this.fill(this.glass());
		this.getInventory().setItem(4, back);
		this.getInventory().setItem(10, mods);
		this.getInventory().setItem(11, chat);
		this.getInventory().setItem(12, team);
		this.getInventory().setItem(13, more);
		return this.getInventory();
	}
	
	public Inventory faqLevels() {
		this.build(27, GeneralLanguage.MENU_GUI_FAQ_LEVELS);
		Server server = Server.get();
		
		ItemStack back = new ItemStack(Material.REDSTONE);
		server.rename(back, this.getLanguage().get(GeneralLanguage.MENU_GUI_BACK));
		
		ItemStack levels = new ItemStack(Material.PAPER);
		server.rename(levels, this.getLanguage().get(GeneralLanguage.MENU_GUI_FAQ_LEVELS_LEVELS));
		
		ItemStack prestiges = new ItemStack(Material.PAPER);
		server.rename(prestiges, this.getLanguage().get(GeneralLanguage.MENU_GUI_FAQ_LEVELS_PRESTIGE));
		
		ItemStack tokens = new ItemStack(Material.PAPER);
		server.rename(tokens, this.getLanguage().get(GeneralLanguage.MENU_GUI_FAQ_LEVELS_TOKENS));
		
		ItemStack prestigeChange = new ItemStack(Material.PAPER);
		server.rename(prestigeChange, this.getLanguage().get(GeneralLanguage.MENU_GUI_FAQ_LEVELS_PRESTIGE_CHANGE));
		
		ItemStack maxLevel = new ItemStack(Material.PAPER);
		server.rename(maxLevel, this.getLanguage().get(GeneralLanguage.MENU_GUI_FAQ_LEVELS_LEVEL_MAX));
		
		ItemStack maxPrestige = new ItemStack(Material.PAPER);
		server.rename(maxPrestige, this.getLanguage().get(GeneralLanguage.MENU_GUI_FAQ_LEVELS_PRESTIGE_MAX));
		
		ItemStack colors = new ItemStack(Material.PAPER);
		server.rename(colors, this.getLanguage().get(GeneralLanguage.MENU_GUI_FAQ_LEVELS_COLORS));

		this.fill(this.glass());
		this.getInventory().setItem(4, back);
		this.getInventory().setItem(10, levels);
		this.getInventory().setItem(11, prestiges);
		this.getInventory().setItem(12, tokens);
		this.getInventory().setItem(13, prestigeChange);
		this.getInventory().setItem(14, maxLevel);
		this.getInventory().setItem(15, maxPrestige);
		this.getInventory().setItem(16, colors);
		return this.getInventory();
	}
	
	public Inventory faqStore() {
		this.build(27, GeneralLanguage.MENU_GUI_FAQ_STORE);
		Server server = Server.get();
		
		ItemStack back = new ItemStack(Material.REDSTONE);
		server.rename(back, this.getLanguage().get(GeneralLanguage.MENU_GUI_BACK));
    	
    	ItemStack where = new ItemStack(Material.PAPER);
    	server.rename(where, this.getLanguage().get(GeneralLanguage.MENU_GUI_FAQ_STORE_KITS));

		this.fill(this.glass());
    	this.getInventory().setItem(4, back);
    	this.getInventory().setItem(10, where);
		return this.getInventory();
	}
	
	public Inventory faqTeamspeak() {
		this.build(27, GeneralLanguage.MENU_GUI_FAQ_TEAMSPEAK);
		Server server = Server.get();
		
		ItemStack back = new ItemStack(Material.REDSTONE);
		server.rename(back, this.getLanguage().get(GeneralLanguage.MENU_GUI_BACK));
    	
    	ItemStack what = new ItemStack(Material.PAPER);
    	server.rename(what, this.getLanguage().get(GeneralLanguage.MENU_GUI_FAQ_TEAMSPEAK_WHAT));
    	
    	ItemStack where = new ItemStack(Material.PAPER);
    	server.rename(where, this.getLanguage().get(GeneralLanguage.MENU_GUI_FAQ_TEAMSPEAK_WHERE));
    	
    	ItemStack ip = new ItemStack(Material.PAPER);
    	server.rename(ip, this.getLanguage().get(GeneralLanguage.MENU_GUI_FAQ_TEAMSPEAK_IP)
				.replaceAll("%name", server.getName()));

		this.fill(this.glass());
    	this.getInventory().setItem(4, back);
    	this.getInventory().setItem(10, what);
    	this.getInventory().setItem(11, where);
    	this.getInventory().setItem(12, ip);
		return this.getInventory();
	}
	
    public Inventory faqUnban() {
		this.build(27, GeneralLanguage.MENU_GUI_FAQ_UNBAN);
		Server server = Server.get();
		
		ItemStack back = new ItemStack(Material.REDSTONE);
		server.rename(back, this.getLanguage().get(GeneralLanguage.MENU_GUI_BACK));
    	
    	ItemStack unban = new ItemStack(Material.PAPER);
    	server.rename(unban, this.getLanguage().get(GeneralLanguage.MENU_GUI_FAQ_UNBAN_UNBAN));
    	
    	ItemStack unmute = new ItemStack(Material.PAPER);
    	server.rename(unmute, this.getLanguage().get(GeneralLanguage.MENU_GUI_FAQ_UNBAN_UNMUTE));
    	
    	ItemStack buy = new ItemStack(Material.PAPER);
    	server.rename(buy, this.getLanguage().get(GeneralLanguage.MENU_GUI_FAQ_UNBAN_WHERE));

		this.fill(this.glass());
    	this.getInventory().setItem(4, back);
    	this.getInventory().setItem(10, unban);
    	this.getInventory().setItem(11, unmute);
    	this.getInventory().setItem(12, buy);
    	return this.getInventory();
	}
    
    public Inventory staffMain(String name) {
		this.build(54, GeneralLanguage.MENU_GUI_STAFF);
		Server server = Server.get();

		StringBuilder builder = new StringBuilder();
		builder.append(this.getLanguage().get(GeneralLanguage.MENU_GUI_STAFF_PREFIX));
		for(Rank rank : Rank.values())
			builder.append("\n" + rank.getPrefix().getColor() + name + ChatColor.GRAY + " = " + rank.getDisplayName());
		ItemStack ranks = new ItemStack(Material.SIGN);
		server.rename(ranks, builder.toString());

		this.fill(this.glass());
		this.getInventory().setItem(4, this.back());
		this.getInventory().setItem(19, admin());
		this.getInventory().setItem(21, mod());
		this.getInventory().setItem(23, supporter());
		this.getInventory().setItem(25, builder());
		this.getInventory().setItem(40, ranks);
    	return this.getInventory();
	}
    
    public Inventory staffAdmin() {
		this.build(54, GeneralLanguage.MENU_GUI_STAFF_ADMINS);
		Server server = Server.get();
		Database db = General.getDB();

		ItemStack glass = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 14);
		server.rename(glass, " ");

		List<String> list = Arrays.asList("server-owner", "admin-1", "admin-2", "admin-3");
		List<ItemStack> heads = list.stream().map(rank -> getHeadData(Prefix.ADMIN, rank)).collect(Collectors.toList());

		this.fill(glass);
		this.getInventory().setItem(0, staff());
		this.getInventory().setItem(3, mod());
		this.getInventory().setItem(5, supporter());
		this.getInventory().setItem(8, builder());
		this.getInventory().setItem(22, heads.get(0));
		this.getInventory().setItem(30, heads.get(1));
		this.getInventory().setItem(31, heads.get(2));
		this.getInventory().setItem(32, heads.get(3));
    	return this.getInventory();
	}

    public Inventory staffMod() {
		this.build(54, GeneralLanguage.MENU_GUI_STAFF_MODS);
		Server server = Server.get();
		Database db = General.getDB();
		
		ItemStack glass = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 10);
		server.rename(glass, " ");

		List<String> modplusList = Arrays.asList("modplus-1", "modplus-2", "modplus-3");
		List<ItemStack> modplus = modplusList.stream().map(rank -> getHeadData(Prefix.MODPLUS, rank)).collect(Collectors.toList());

		List<String> modList = Arrays.asList("mod-1", "mod-2", "mod-3", "mod-4", "mod-5", "mod-6", "mod-7", "trialmod-1", "trialmod-2", "trialmod-3");
		List<ItemStack> mod = modList.stream().map(rank -> getHeadData(Prefix.MOD, rank)).collect(Collectors.toList());

		this.fill(glass);
		this.getInventory().setItem(0, admin());
		this.getInventory().setItem(3, staff());
		this.getInventory().setItem(5, supporter());
		this.getInventory().setItem(8, builder());
		this.getInventory().setItem(21, modplus.get(0));
		this.getInventory().setItem(22, modplus.get(1));
		this.getInventory().setItem(23, modplus.get(2));
		this.getInventory().setItem(28, mod.get(0));
		this.getInventory().setItem(29, mod.get(1));
		this.getInventory().setItem(30, mod.get(2));
		this.getInventory().setItem(31, mod.get(3));
		this.getInventory().setItem(32, mod.get(4));
		this.getInventory().setItem(33, mod.get(5));
		this.getInventory().setItem(34, mod.get(6));
		this.getInventory().setItem(39, mod.get(7));
		this.getInventory().setItem(40, mod.get(8));
		this.getInventory().setItem(41, mod.get(9));
    	return this.getInventory();
	}

	public Inventory staffSupporter() {
		this.build(54, GeneralLanguage.MENU_GUI_STAFF_SUPPORTERS);
		Server server = Server.get();
		Database db = General.getDB();

		ItemStack glass = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 6);
		server.rename(glass, " ");

		List<String> list = Arrays.asList("supporter-1", "supporter-2", "supporter-3", "supporter-4", "supporter-5");
		List<ItemStack> heads = list.stream().map(rank -> getHeadData(Prefix.SUPPORTER, rank)).collect(Collectors.toList());

		this.fill(glass);
		this.getInventory().setItem(0, admin());
		this.getInventory().setItem(3, mod());
		this.getInventory().setItem(5, staff());
		this.getInventory().setItem(8, builder());
		this.getInventory().setItem(20, heads.get(0));
		this.getInventory().setItem(21, heads.get(1));
		this.getInventory().setItem(22, heads.get(2));
		this.getInventory().setItem(23, heads.get(3));
		this.getInventory().setItem(24, heads.get(4));
		return this.getInventory();
	}
    
    public Inventory staffBuilder() {
		this.build(54, GeneralLanguage.MENU_GUI_STAFF_BUILDERS);
		Server server = Server.get();
		Database db = General.getDB();

		List<String> list = Arrays.asList("builder-1", "builder-2", "builder-3");
		List<ItemStack> heads = list.stream().map(rank -> getHeadData(Prefix.BUILDER, rank)).collect(Collectors.toList());

		ItemStack glass = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 5);
		server.rename(glass, " ");

		this.fill(glass);
		this.getInventory().setItem(0, admin());
		this.getInventory().setItem(3, mod());
		this.getInventory().setItem(5, supporter());
		this.getInventory().setItem(8, staff());
		this.getInventory().setItem(21, heads.get(0));
		this.getInventory().setItem(22, heads.get(1));
		this.getInventory().setItem(23, heads.get(2));
    	return this.getInventory();
	}

	private ItemStack getHeadData(Prefix prefix, String rank) {
		Database db = General.getDB();
    	Server server = Server.get();
		ItemStack head = new ItemStack(Material.BARRIER);
		server.rename(head, this.getLanguage().get(GeneralLanguage.DB_CHECK_FAIL));
		try {
			ResultSet res = db.select(General.DB_STAFF, "rank", rank);
			if(res.next()) {
				String StringUUID = res.getString("uuid");
				if(StringUUID.equalsIgnoreCase("You?")) {
					head = server.playerHead("MHF_Question");
					server.rename(head, prefix.getColor() + this.getLanguage().get(GeneralLanguage.MENU_GUI_STAFF_YOU) + "\n&7Click to apply");
					return head;
				} else {
					UUID uuid = UUID.fromString(StringUUID);
					OfflinePlayer offp = Bukkit.getOfflinePlayer(uuid);
					head = server.playerHead(offp.getName());
					ePlayer p = ePlayer.get(offp.getUniqueId());
					if(p != null && !p.isInvis())
						server.rename(head, prefix.getColor() + offp.getName() + "\n" + role(res.getString("role")) + "\n" + "&7[&aONLINE&7]");
					else
						server.rename(head, prefix.getColor() + offp.getName() + "\n" + role(res.getString("role")) + "\n" + "&7[&cOFFLINE&7]");
					return head;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return head;
	}
    
    private ItemStack staff() {
    	Server server = Server.get();
		ItemStack Staff = new ItemStack(Material.STAINED_GLASS, 1);
		server.rename(Staff, this.getLanguage().get(GeneralLanguage.MENU_GUI_STAFF));
		return Staff;
	}
	
	private ItemStack admin() {
		Server server = Server.get();
		ItemStack Admin = new ItemStack(Material.STAINED_GLASS, 1, (short) 14);
		server.rename(Admin, this.getLanguage().get(GeneralLanguage.MENU_GUI_STAFF_ADMINS));
		return Admin;
	}
	
	private ItemStack mod() {
		Server server = Server.get();
		ItemStack Mod = new ItemStack(Material.STAINED_GLASS, 1, (short) 10);
		server.rename(Mod, this.getLanguage().get(GeneralLanguage.MENU_GUI_STAFF_MODS));
		return Mod;
	}

	private ItemStack supporter() {
		Server server = Server.get();
		ItemStack supp = new ItemStack(Material.STAINED_GLASS, 1, (short) 6);
		server.rename(supp, this.getLanguage().get(GeneralLanguage.MENU_GUI_STAFF_SUPPORTERS));
		return supp;
	}
	
	private ItemStack builder() {
		Server server = Server.get();
		ItemStack Builder = new ItemStack(Material.STAINED_GLASS, 1, (short) 5);
		server.rename(Builder, this.getLanguage().get(GeneralLanguage.MENU_GUI_STAFF_BUILDERS));
		return Builder;
	}
	
	private String role(String string) {
		return ChatColor.GRAY + "(" + ChatColor.translateAlternateColorCodes('&', string)+ ChatColor.RESET + ChatColor.GRAY + ")";
	}
}