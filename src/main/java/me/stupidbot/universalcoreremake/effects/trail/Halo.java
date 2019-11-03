package me.stupidbot.universalcoreremake.effects.trail;

// --Commented out by Inspection START (10/30/2019 3:03 PM):
//class Halo extends Effect {
//    private int step = 0;
//
//// --Commented out by Inspection START (10/30/2019 3:00 PM):
////    public Halo(EffectManager effectManager) {
////        super(effectManager);
////        type = EffectType.REPEATING;
////        iterations = -1; // Infinite by default;
////    }
//// --Commented out by Inspection STOP (10/30/2019 3:00 PM)
//
//    @Override
//    public void onRun() {
//        Location loc = getLocation();
//
//        double dx = 0.75 * Math.sin(Math.PI / 8 * step);
//        double dy = 2;
//        double dz = 0.75 * Math.cos(Math.PI / 8 * step);
//        loc.add(dx, dy, dz);
//
//        display(ParticleEffect.REDSTONE, loc, Color.fromRGB(255, 170, 0));
//
//
//        step++;
//        if (step > 8)
//            step = 0;
//    }
// --Commented out by Inspection STOP (10/30/2019 3:03 PM)
//}