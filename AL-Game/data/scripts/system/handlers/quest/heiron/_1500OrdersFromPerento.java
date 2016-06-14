/**
 * This file is part of Aion-Lightning <aion-lightning.org>.
 *
 *  Aion-Lightning is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  Aion-Lightning is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details. *
 *  You should have received a copy of the GNU General Public License
 *  along with Aion-Lightning.
 *  If not, see <http://www.gnu.org/licenses/>.
 */
package quest.heiron;

import com.aionemu.gameserver.model.gameobjects.Npc;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.questEngine.QuestEngine;
import com.aionemu.gameserver.questEngine.handlers.QuestHandler;
import com.aionemu.gameserver.model.DialogAction;
import com.aionemu.gameserver.model.PlayerClass;
import com.aionemu.gameserver.questEngine.model.QuestEnv;
import com.aionemu.gameserver.questEngine.model.QuestState;
import com.aionemu.gameserver.questEngine.model.QuestStatus;
import com.aionemu.gameserver.world.zone.ZoneName;

/**
 * @author MrPoke + Dune11
 */
public class _1500OrdersFromPerento extends QuestHandler {

    private final static int questId = 1500;

    public _1500OrdersFromPerento() {
        super(questId);
    }

    @Override
    public void register() {
        qe.registerQuestNpc(204500).addOnTalkEvent(questId);
        qe.registerOnEnterZone(ZoneName.get("NEW_HEIRON_GATE_210040000"), questId);
    }

    @Override
    public boolean onDialogEvent(QuestEnv env) {
        final Player player = env.getPlayer();
        final QuestState qs = player.getQuestStateList().getQuestState(questId);
        if (qs == null) {
            return false;
        }

        int targetId = 0;
        if (env.getVisibleObject() instanceof Npc) {
            targetId = ((Npc) env.getVisibleObject()).getNpcId();
        }
        if (targetId != 204500) {
            return false;
        }
        if (qs.getStatus() == QuestStatus.START) {
            if (env.getDialog() == DialogAction.QUEST_SELECT) {
                return sendQuestDialog(env, 10002);
            } else if (env.getDialogId() == DialogAction.SELECT_QUEST_REWARD.id()) {
                qs.setStatus(QuestStatus.REWARD);
                qs.setQuestVarById(0, 1);
                updateQuestStatus(env);
                return sendQuestDialog(env, 5);
            }
            return false;
        } else if (qs.getStatus() == QuestStatus.REWARD) {
            if (env.getDialogId() == DialogAction.SELECTED_QUEST_NOREWARD.id()) {
                int[] ids = {1051, 1052, 1053, 1054, 1055, 1056, 1057, 1058, 1059, 1062, 1063};
                for (int id : ids) {
                    QuestEngine.getInstance().onEnterZoneMissionEnd(
                            new QuestEnv(env.getVisibleObject(), env.getPlayer(), id, env.getDialogId()));
                }
            }
            return sendQuestEndDialog(env);
        }
        return false;
    }

    @Override
    public boolean onEnterZoneEvent(QuestEnv env, ZoneName zoneName) {
        Player player = env.getPlayer();
        PlayerClass playerClass = player.getCommonData().getPlayerClass();
            if (playerClass == PlayerClass.WARRIOR || playerClass == PlayerClass.SCOUT || playerClass == PlayerClass.MAGE || playerClass == PlayerClass.PRIEST
                || playerClass == PlayerClass.ENGINEER || playerClass == PlayerClass.ARTIST || playerClass == PlayerClass.GLADIATOR || playerClass == PlayerClass.TEMPLAR
                || playerClass == PlayerClass.ASSASSIN  || playerClass == PlayerClass.RANGER || playerClass == PlayerClass.SORCERER || playerClass == PlayerClass.SPIRIT_MASTER
                || playerClass == PlayerClass.CHANTER || playerClass == PlayerClass.CLERIC || playerClass == PlayerClass.GUNNER || playerClass == PlayerClass.BARD || playerClass == PlayerClass.RIDER) {
            return false;
            }
        return defaultOnEnterZoneEvent(env, zoneName, ZoneName.get("NEW_HEIRON_GATE_210040000"));
    }
}
