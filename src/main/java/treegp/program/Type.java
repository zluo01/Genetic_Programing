package treegp.program;

import genetics.utils.Observation;

public enum Type implements Operator {

    CONSTANT {
        @Override
        public int argumentCount() {
            return 0;
        }

        @Override
        public boolean isVariable() {
            return false;
        }

        @Override
        public boolean isNumber() {
            return true;
        }

        @Override
        public String toString(TreeNode treeNode) {
            double retVal = treeNode.getValue();
            return retVal < 0 ? String.format("(%s)", retVal) : "" + retVal;
        }

        @Override
        public double eval(TreeNode treeNode, Observation observation) {
            return treeNode.getValue();
        }
    },
    VARIABLE {
        @Override
        public int argumentCount() {
            return 0;
        }

        @Override
        public boolean isVariable() {
            return true;
        }

        @Override
        public boolean isNumber() {
            return false;
        }

        @Override
        public String toString(TreeNode treeNode) {
            return treeNode.getVariable();
        }

        @Override
        public double eval(TreeNode treeNode, Observation observation) {
            return treeNode.getManager().lookupVariable(treeNode.getVariable());
        }
    },
    ADD {
        @Override
        public int argumentCount() {
            return 2;
        }

        @Override
        public boolean isVariable() {
            return false;
        }

        @Override
        public boolean isNumber() {
            return false;
        }

        @Override
        public String toString(TreeNode treeNode) {
            return String.format("(%s + %s)", treeNode.getLeft(), treeNode.getRight());
        }

        @Override
        public double eval(TreeNode treeNode, Observation observation) {
            double left = treeNode.getLeft().eval(observation);
            double right = treeNode.getRight().eval(observation);
            return left + right;
        }
    },
    SUB {
        @Override
        public int argumentCount() {
            return 2;
        }

        @Override
        public boolean isVariable() {
            return false;
        }

        @Override
        public boolean isNumber() {
            return false;
        }

        @Override
        public String toString(TreeNode treeNode) {
            return String.format("(%s - %s)", treeNode.getLeft(), treeNode.getRight());
        }

        @Override
        public double eval(TreeNode treeNode, Observation observation) {
            double left = treeNode.getLeft().eval(observation);
            double right = treeNode.getRight().eval(observation);
            return left - right;
        }
    },
    MUL {
        @Override
        public int argumentCount() {
            return 2;
        }

        @Override
        public boolean isVariable() {
            return false;
        }

        @Override
        public boolean isNumber() {
            return false;
        }

        @Override
        public String toString(TreeNode treeNode) {
            return String.format("(%s * %s)", treeNode.getLeft(), treeNode.getRight());
        }

        @Override
        public double eval(TreeNode treeNode, Observation observation) {
            double left = treeNode.getLeft().eval(observation);
            double right = treeNode.getRight().eval(observation);
            return left * right;
        }
    },
    DIV {
        @Override
        public int argumentCount() {
            return 2;
        }

        @Override
        public boolean isVariable() {
            return false;
        }

        @Override
        public boolean isNumber() {
            return false;
        }

        public String toString(TreeNode treeNode) {
            return String.format("(%s / %s)", treeNode.getLeft(), treeNode.getRight());
        }

        @Override
        public double eval(TreeNode treeNode, Observation observation) {
            double left = treeNode.getLeft().eval(observation);
            double right = treeNode.getRight().eval(observation);
            return right == 0 ? Double.MAX_VALUE : left / right;
        }
    },
    POW {
        @Override
        public int argumentCount() {
            return 2;
        }

        @Override
        public boolean isVariable() {
            return false;
        }

        @Override
        public boolean isNumber() {
            return false;
        }

        @Override
        public String toString(TreeNode treeNode) {
            return String.format("(%s ^ %s)", treeNode.getLeft(), treeNode.getRight());
        }

        @Override
        public double eval(TreeNode treeNode, Observation observation) {
            double left = treeNode.getLeft().eval(observation);
            double right = treeNode.getRight().eval(observation);
            return Math.pow(left, right);
        }
    },
    SQRT {
        @Override
        public int argumentCount() {
            return 1;
        }

        @Override
        public boolean isVariable() {
            return false;
        }

        @Override
        public boolean isNumber() {
            return false;
        }

        public String toString(TreeNode treeNode) {
            return String.format("sqrt(%s)", treeNode.getLeft());
        }

        @Override
        public double eval(TreeNode treeNode, Observation observation) {
            double arg = treeNode.getLeft().eval(observation);
            return arg < 0 ? Double.MAX_VALUE : Math.sqrt(arg);
        }
    },
    LN {

        private static final double threshold = 1e-5;

        @Override
        public int argumentCount() {
            return 1;
        }

        @Override
        public boolean isVariable() {
            return false;
        }

        @Override
        public boolean isNumber() {
            return false;
        }

        @Override
        public String toString(TreeNode treeNode) {
            return String.format("ln(%s)", treeNode.getLeft());
        }

        @Override
        public double eval(TreeNode treeNode, Observation observation) {
            double arg = treeNode.getLeft().eval(observation);
            return arg < 0 ? Double.MAX_VALUE : Math.log(arg + threshold);
        }
    },
    SIN {
        @Override
        public int argumentCount() {
            return 1;
        }

        @Override
        public boolean isVariable() {
            return false;
        }

        @Override
        public boolean isNumber() {
            return false;
        }

        @Override
        public String toString(TreeNode treeNode) {
            return String.format("sin(%s)", treeNode.getLeft());
        }

        @Override
        public double eval(TreeNode treeNode, Observation observation) {
            double arg = treeNode.getLeft().eval(observation);
            return Math.sin(arg);
        }
    },
    COS {
        @Override
        public int argumentCount() {
            return 1;
        }

        @Override
        public boolean isVariable() {
            return false;
        }

        @Override
        public boolean isNumber() {
            return false;
        }

        public String toString(TreeNode treeNode) {
            return String.format("cos(%s)", treeNode.getLeft());
        }

        @Override
        public double eval(TreeNode treeNode, Observation observation) {
            double arg = treeNode.getLeft().eval(observation);
            return Math.cos(arg);
        }
    },

    TAN {
        @Override
        public int argumentCount() {
            return 1;
        }

        @Override
        public boolean isVariable() {
            return false;
        }

        @Override
        public boolean isNumber() {
            return false;
        }

        @Override
        public String toString(TreeNode treeNode) {
            return String.format("tan(%s)", treeNode.getLeft());
        }

        @Override
        public double eval(TreeNode treeNode, Observation observation) {
            double arg = treeNode.getLeft().eval(observation);
            return Math.tan(arg);
        }
    },

    AND {

        private static final double mTolerance = 0.0000001;

        @Override
        public int argumentCount() {
            return 2;
        }

        @Override
        public boolean isVariable() {
            return false;
        }

        @Override
        public boolean isNumber() {
            return false;
        }

        @Override
        public String toString(TreeNode treeNode) {
            return String.format("(%s AND %s)", treeNode.getLeft(), treeNode.getRight());
        }

        @Override
        public double eval(TreeNode treeNode, Observation observation) {
            return isTrue(treeNode.getLeft().eval(observation)) && isTrue(treeNode.getRight().eval(observation)) ? 1 : 0;
        }

        private boolean isTrue(double x) {
            return !(x > -mTolerance) || !(x < mTolerance);
        }
    },

    EXP {
        @Override
        public int argumentCount() {
            return 1;
        }

        @Override
        public boolean isVariable() {
            return false;
        }

        @Override
        public boolean isNumber() {
            return false;
        }

        @Override
        public String toString(TreeNode treeNode) {
            return String.format("exp(%s)", treeNode.getLeft());
        }

        @Override
        public double eval(TreeNode treeNode, Observation observation) {
            return Math.exp(treeNode.getLeft().eval(observation));
        }
    },

    MOD {
        @Override
        public int argumentCount() {
            return 2;
        }

        @Override
        public boolean isVariable() {
            return false;
        }

        @Override
        public boolean isNumber() {
            return false;
        }

        @Override
        public String toString(TreeNode treeNode) {
            return "(" + treeNode.getLeft() + " % " + treeNode.getRight() + ")";
        }

        @Override
        public double eval(TreeNode treeNode, Observation observation) {
            double left = treeNode.getLeft().eval(observation);
            double right = treeNode.getRight().eval(observation);
            return right == 0 ? Double.MAX_VALUE : left % right;
        }
    },

    NOT {

        private static final double tolerance = 0.0000001;

        @Override
        public int argumentCount() {
            return 1;
        }

        @Override
        public boolean isVariable() {
            return false;
        }

        @Override
        public boolean isNumber() {
            return false;
        }

        @Override
        public String toString(TreeNode treeNode) {
            return String.format("(NOT %s)", treeNode.getLeft());
        }

        @Override
        public double eval(TreeNode treeNode, Observation observation) {
            return isTrue(treeNode.getLeft().eval(observation)) ? 1 : 0;
        }

        private boolean isTrue(double x) {
            return !(x > -tolerance) || !(x < tolerance);
        }
    },

    OR {
        private static final double mTolerance = 0.0000001;

        @Override
        public int argumentCount() {
            return 2;
        }

        @Override
        public boolean isVariable() {
            return false;
        }

        @Override
        public boolean isNumber() {
            return false;
        }

        @Override
        public String toString(TreeNode treeNode) {
            return String.format("(%s OR %s)", treeNode.getLeft(), treeNode.getRight());
        }

        @Override
        public double eval(TreeNode treeNode, Observation observation) {
            return isTrue(treeNode.getLeft().eval(observation)) || isTrue(treeNode.getRight().eval(observation)) ? 1 : 0;
        }

        private boolean isTrue(double x) {
            return !(x > -mTolerance) || !(x < mTolerance);
        }
    }
}
