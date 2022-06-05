package net.minecraft.src;

import net.minecraft.entity.EntityList;

public class EntityUtils
{
    public static int getEntityId(String p_getEntityId_0_)
    {
        if (p_getEntityId_0_ == null)
        {
            return -1;
        }
        else
        {
            int i = EntityList.getIDFromString(p_getEntityId_0_);

            if (i < 0)
            {
                return -1;
            }
            else
            {
                if (i == 90)
                {
                    Class oclass = EntityList.getClassFromID(i);

                    if (oclass == null)
                    {
                        return -1;
                    }

                    String s = EntityList.getEntityStringFromClass(oclass);

                    if (!Config.equals(p_getEntityId_0_, s))
                    {
                        return -1;
                    }
                }

                return i;
            }
        }
    }

    public static Class getEntityClass(String p_getEntityClass_0_)
    {
        int i = getEntityId(p_getEntityClass_0_);

        if (i < 0)
        {
            return null;
        }
        else
        {
            Class oclass = EntityList.getClassFromID(i);
            return oclass;
        }
    }
}
