package tech.hadenw.icontrolu.plugin;

import java.lang.reflect.Field;

import org.bukkit.enchantments.Enchantment;
import org.bukkit.enchantments.EnchantmentTarget;
import org.bukkit.enchantments.EnchantmentWrapper;
import org.bukkit.inventory.ItemStack;

public class EnchantGlow extends EnchantmentWrapper
{
	
	private static Enchantment glow;
	
	public EnchantGlow(String name )
	{
		super(name);
	}
	
	@Override
	public boolean canEnchantItem(ItemStack item)
	{
		return true;
	}
	
	@Override
	public boolean conflictsWith(Enchantment other)
	{
		return false;
	}
	
	@Override
	public EnchantmentTarget getItemTarget()
	{
		return null;
	}
	
	@Override
	public int getMaxLevel()
	{
		return 10;
	}
	
	@Override
	public String getName()
	{
		return "icuenchglow";
	}
	
	@Override
	public int getStartLevel()
	{
		return 1;
	}
	
	public static Enchantment getGlow()
	{
		if ( glow != null )
			return glow;
		
		try
		{
			Field f = Enchantment.class.getDeclaredField("acceptingNew");
			f.setAccessible(true);
			f.set(null , true);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		
		glow = new EnchantGlow("icuenchglow");
		Enchantment.registerEnchantment(glow);
		return glow;
	}
	
	public static ItemStack addGlow(ItemStack item)
	{
		Enchantment glow = getGlow();
		
		item.addEnchantment(glow , 1);
		return item;
	}
	
}
