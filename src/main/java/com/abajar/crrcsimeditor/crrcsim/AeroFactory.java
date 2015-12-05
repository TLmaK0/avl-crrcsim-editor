/*
 * Copyright (C) 2015  Hugo Freire Gil 
 * 
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 */
package com.abajar.crrcsimeditor.crrcsim;

import com.abajar.crrcsimeditor.UnitConversor;
import com.abajar.crrcsimeditor.avl.AVL;
import com.abajar.crrcsimeditor.avl.connectivity.AvlRunner;
import com.abajar.crrcsimeditor.avl.runcase.AvlCalculation;
import com.abajar.crrcsimeditor.avl.runcase.Configuration;
import com.abajar.crrcsimeditor.avl.runcase.StabilityDerivatives;
import java.io.IOException;
import java.nio.file.Path;

/**
 *
 * @author Hugo
 */
public class AeroFactory {
    public Aero createFromAvl(String avlPath, AVL avl, Path originPath) throws IOException, InterruptedException, Exception{
        AvlRunner avlRunner = new AvlRunner(avlPath, avl, originPath);
        
        AvlCalculation avlCalculation = avlRunner.getCalculation();
        Aero aero = new Aero();

        int elevatorPosition = avlCalculation.getElevatorPosition();
        int rudderPosition = avlCalculation.getRudderPosition();
        int aileronPosition = avlCalculation.getAileronPosition();

        StabilityDerivatives std = avlCalculation.getStabilityDerivatives();
        Configuration config = avlCalculation.getConfiguration();

        Reference ref = aero.getRef();
        UnitConversor uc = new UnitConversor();
        ref.setChord(uc.convertToMeters(config.getCref(), avl.getLengthUnit()));
        ref.setSpan(uc.convertToMeters(config.getBref(), avl.getLengthUnit()));
        ref.setArea(uc.convertToSquareMeters(config.getSref(), avl.getLengthUnit()));
        ref.setSpeed(config.getVelocity());

        Miscellaneous misc = aero.getMisc();
        misc.setAlpha_0((float)(config.getAlpha() * Math.PI / 180));

        misc.setEta_loc(0.3f); //eta_loc for stall model http://en.wikipedia.org/wiki/Pseudorapidity
        misc.setCG_arm(0.25f); //The typical value CG_arm = 0.25 means that the point of application of the averaged dCL is 0.25*chord ahead of the CG.
        misc.setSpan_eff(config.getE()); //span efficiency: Effective span, 0.95 for most planes, 0.85 flying wing.

        //TODO: eta_loc, CG_arm, span_eff add to editor

        PitchMoment pitchMoment = aero.getPitchMoment();
        pitchMoment.setCm_0(config.getCmtot());
        pitchMoment.setCm_a(std.getCma());
        pitchMoment.setCm_q(std.getCmq());
        if (elevatorPosition != -1) pitchMoment.setCm_de(std.getCmd()[elevatorPosition]);

        Lift lift = aero.getLift();
        lift.setCL_0(config.getCLtot());

        //TODO: CL_max, CL_min add to editor
        lift.setCL_max(1.1f);
        lift.setCL_min(-0.6f);

        lift.setCL_a(std.getCLa());
        lift.setCL_q(std.getCLq());
        if (elevatorPosition != -1) lift.setCL_de(std.getCLd()[elevatorPosition]);
        lift.setCL_drop(0.1f);     //CL drop during stall break //TODO: CL_drop add to editor
        lift.setCL_CD0(0);      //CL at minimum profile //TODO: CL_CD0 add to editor  // 0.30 for 7037, 0.15 MH32, 0.0 RG15, AGxx, power
        lift.setCL_0(config.getCLtot());

        Drag drag = aero.getDrag();
        drag.setCD_prof(config.getCDvis());

        drag.setUexp_CD(0.5f); //for Re-scaling of CD_prof  ~ (U/U_ref)^Uexp_CD //TODO: Uexp_CD add to editor
        drag.setCD_stall(-0.5f); //drag coeff. during stalling //TODO: CD_stall add to editor
        drag.setCD_CLsq(0.01f); //d(CD)/d(CL^2), curvature of parabolic profile polar: 0.01 composites, 0.015 saggy ships, 0.02 beat up ship //TODO: CD_CLsq add to editor
        drag.setCD_AIsq(0.01f); //drag due to aileron deflection d(CD)/d(aileron^2) , curvature of ail. CD influence: 0.01/(max_aileron)^2  //TODO: CD_AIsq add to editor //
        drag.setCD_ELsq(0f); //drag due to elevon deflection d(CD)/d(elevator^2), curvature of ele. CD influence: 0.01/(max_elevator)^2 for Zagi otherwise 0 //TODO: CD_ELsq add to editor  //

        Y Y = aero.getSideForce();
        Y.setCY_b(std.getCYb());
        Y.setCY_p(std.getCYp());
        Y.setCY_r(std.getCYr());
        if (rudderPosition != -1) Y.setCY_dr(std.getCYd()[rudderPosition]);
        if (aileronPosition != -1)Y.setCY_da(std.getCYd()[aileronPosition]);

        l l = aero.getRollMomment();
        l.setCl_b(std.getClb());
        l.setCl_p(std.getClp());
        l.setCl_r(std.getClr());
        if (rudderPosition != -1) l.setCl_dr(std.getCld()[rudderPosition]);
        if (aileronPosition != -1) l.setCl_da(std.getCld()[aileronPosition]);

        n n = aero.getYawMomment();
        n.setCn_b(std.getCnb());
        n.setCn_p(std.getCnp());
        n.setCn_r(std.getCnr());
        if (rudderPosition != -1) n.setCn_dr(std.getCnd()[rudderPosition]);
        if (aileronPosition != -1)n.setCn_da(std.getCnd()[aileronPosition]);

        //TODO: add flap section
        //TODO: add spoilder section
        //TODO: add retract section

        return aero;
    }
}