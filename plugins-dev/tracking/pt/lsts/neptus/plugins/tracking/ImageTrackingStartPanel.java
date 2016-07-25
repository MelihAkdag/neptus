/*
 * Copyright (c) 2004-2016 Universidade do Porto - Faculdade de Engenharia
 * Laboratório de Sistemas e Tecnologia Subaquática (LSTS)
 * All rights reserved.
 * Rua Dr. Roberto Frias s/n, sala I203, 4200-465 Porto, Portugal
 *
 * This file is part of Neptus, Command and Control Framework.
 *
 * Commercial Licence Usage
 * Licencees holding valid commercial Neptus licences may use this file
 * in accordance with the commercial licence agreement provided with the
 * Software or, alternatively, in accordance with the terms contained in a
 * written agreement between you and Universidade do Porto. For licensing
 * terms, conditions, and further information contact lsts@fe.up.pt.
 *
 * European Union Public Licence - EUPL v.1.1 Usage
 * Alternatively, this file may be used under the terms of the EUPL,
 * Version 1.1 only (the "Licence"), appearing in the file LICENSE.md
 * included in the packaging of this file. You may not use this work
 * except in compliance with the Licence. Unless required by applicable
 * law or agreed to in writing, software distributed under the Licence is
 * distributed on an "AS IS" basis, WITHOUT WARRANTIES OR CONDITIONS OF
 * ANY KIND, either express or implied. See the Licence for the specific
 * language governing permissions and limitations at
 * https://www.lsts.pt/neptus/licence.
 *
 * For more information please see <http://lsts.fe.up.pt/neptus>.
 *
 * Author: pdias
 * Jul 7, 2016
 */
package pt.lsts.neptus.plugins.tracking;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ImageIcon;

import pt.lsts.imc.ImageTracking;
import pt.lsts.imc.PlanControl;
import pt.lsts.neptus.NeptusLog;
import pt.lsts.neptus.comm.IMCSendMessageUtils;
import pt.lsts.neptus.console.ConsoleLayout;
import pt.lsts.neptus.console.ConsolePanel;
import pt.lsts.neptus.console.plugins.LockableSubPanel;
import pt.lsts.neptus.gui.ToolbarButton;
import pt.lsts.neptus.i18n.I18n;
import pt.lsts.neptus.plugins.PluginDescription;
import pt.lsts.neptus.plugins.PluginDescription.CATEGORY;
import pt.lsts.neptus.util.ImageUtils;

/**
 * @author pdias
 *
 */
@SuppressWarnings("serial")
@PluginDescription(name = "Image Tracking Start Panel", author = "Paulo Dias", version = "0.1.0", category = CATEGORY.PLANNING,
    icon = "pt/lsts/neptus/plugins/tracking/tracking.png")
public class ImageTrackingStartPanel extends ConsolePanel implements LockableSubPanel {

    private final ImageIcon ICON_BTN = ImageUtils.getScaledIcon("pt/lsts/neptus/plugins/tracking/tracking.png", 32, 32);

    private ToolbarButton triggerButton;
    private AbstractAction triggerAction;
    private final String triggerStr = I18n.text("Start Image Tracking");
    
    private boolean locked = false;
    
    /**
     * @param console
     */
    public ImageTrackingStartPanel(ConsoleLayout console) {
        super(console);
        initialize();
    }
    
    private void initialize() {
        initializeActions();
        
        removeAll();
        setSize(new Dimension(60, 60));
        setLayout(new BorderLayout());
        
        triggerButton = new ToolbarButton(triggerAction);
        add(triggerButton);
    }

    /**
     * 
     */
    private void initializeActions() {
        triggerAction = new AbstractAction(triggerStr, ICON_BTN) {
            @Override
            public void actionPerformed(final ActionEvent ev) {
                final Object action = getValue(Action.NAME);
                NeptusLog.action().debug(action);
                PlanControl pc = new PlanControl();
                pc.setType(PlanControl.TYPE.REQUEST);
                pc.setOp(PlanControl.OP.START);
                pc.setRequestId(IMCSendMessageUtils.getNextRequestId());
                pc.setPlanId("imagetracking");
                pc.setArg(new ImageTracking());
                send(pc);
            };
        };
    }

    /* (non-Javadoc)
     * @see pt.lsts.neptus.console.ConsolePanel#initSubPanel()
     */
    @Override
    public void initSubPanel() {
    }

    /* (non-Javadoc)
     * @see pt.lsts.neptus.console.ConsolePanel#cleanSubPanel()
     */
    @Override
    public void cleanSubPanel() {
    }

    /* (non-Javadoc)
     * @see pt.lsts.neptus.console.plugins.LockableSubPanel#lock()
     */
    @Override
    public void lock() {
        locked = false;
        refreshUI();
    }

    /* (non-Javadoc)
     * @see pt.lsts.neptus.console.plugins.LockableSubPanel#unLock()
     */
    @Override
    public void unLock() {
        locked = false;
        refreshUI();
    }

    /* (non-Javadoc)
     * @see pt.lsts.neptus.console.plugins.LockableSubPanel#isLocked()
     */
    @Override
    public boolean isLocked() {
        return locked;
    }
    
    private void refreshUI() {
        triggerButton.setEnabled(locked);
    }
}