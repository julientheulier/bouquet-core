/*******************************************************************************
 * Copyright © Squid Solutions, 2016
 *
 * This file is part of Open Bouquet software.
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation (version 3 of the License).
 *
 * There is a special FOSS exception to the terms and conditions of the
 * licenses as they are applied to this program. See LICENSE.txt in
 * the directory of this program distribution.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 *
 * Squid Solutions also offers commercial licenses with additional warranties,
 * professional functionalities or services. If you purchase a commercial
 * license, then it supersedes and replaces any other agreement between
 * you and Squid Solutions (above licenses and LICENSE.txt included).
 * See http://www.squidsolutions.com/EnterpriseBouquet/
 *******************************************************************************/
package com.squid.core.domain.operators;

import java.util.*;

import com.squid.core.domain.IDomain;
import com.squid.core.domain.operators.special.DivideOperatorDefinition;
import com.squid.core.domain.operators.special.MinusOperatorDefinition;
import com.squid.core.domain.vector.VectorOperatorDefinition;
import com.squid.core.expression.scope.ScopeException;

/**
 * @author serge fantino
 */
public class OperatorScope implements IntrinsicOperators {

  private static OperatorScope g_default = new OperatorScope();

  public static OperatorScope getDefault() {
    return g_default;
  }

  private Hashtable<String, OperatorDefinition> m_lookupByName = new Hashtable<String, OperatorDefinition>();
  private Vector<OperatorDefinition> m_lookupByID = new Vector<OperatorDefinition>();
  private ArrayList<OperatorDefinition> m_algebraicList = new ArrayList<OperatorDefinition>();
  private ArrayList<OperatorDefinition> m_aggregateList = new ArrayList<OperatorDefinition>();

  /**
   * Support for extendedID
   */
  private Hashtable<String, OperatorDefinition> lookupByExtendedID = new Hashtable<String, OperatorDefinition>();

  private UndefinedOperatorDefinition undefinedOperatorDef = new UndefinedOperatorDefinition("UNDEFINED", UNDEFINED_ID);

  private OperatorScopeRegistry registry = null;

  public OperatorScope() {
    initIntrinsicOperators();
    //
    registry = new OperatorScopeRegistry();
    registry.loadExtensions(this);
  }

  /**
   *
   */
  private void initIntrinsicOperators() {
    registerSafe(undefinedOperatorDef);
    // -----------------------------------------------------------------------------
    registerSafe(new BinaryArithmeticOperatorDefintion("CONCAT", CONCAT, IDomain.STRING, OperatorDefinition.STRING_TYPE));
    // -----------------------------------------------------------------------------
    registerSafe(new AdditiveOperatorDefinition("PLUS", PLUS, "+", IDomain.NUMERIC));
    registerSafe(new AdditiveOperatorDefinition("SUBTRACTION", SUBTRACTION, "-", IDomain.NUMERIC));
    registerSafe(new MinusOperatorDefinition("MINUS", MINUS, OperatorDefinition.PREFIX_POSITION, "-", IDomain.NUMERIC, OperatorDefinition.MATHS_TYPE));
    registerSafe(new DivideOperatorDefinition("DIVIDE", DIVIDE, "/", IDomain.NUMERIC));
    // registerSafe(new ArithmeticOperatorDefintion("DIVIDE",DIVIDE,"/",IDomain.NUMERIC));
    registerSafe(new BinaryArithmeticOperatorDefintion("MULTIPLY", MULTIPLY, "*", IDomain.NUMERIC));
    registerSafe(new UnaryArithmeticOperatorDefintion("EXPONENTIATE", EXPONENTIATE, "**", IDomain.NUMERIC));
    registerSafe(new BinaryArithmeticOperatorDefintion("MODULO", MODULO, "%", IDomain.NUMERIC));
    registerSafe(new UnaryArithmeticOperatorDefintion("ABS", ABS, IDomain.NUMERIC, OperatorDefinition.MATHS_TYPE));
    // -----------------------------------------------------------------------------
    final String[] ops = { "EXP", "LN", "LOG", "SQRT" };
    for (String op : ops) {
      registerSafe(new UnaryArithmeticOperatorDefintion(op, "com.squid.domain.opertor.extension." + op, OperatorDefinition.PREFIX_POSITION, op, IDomain.CONTINUOUS, OperatorDefinition.MATHS_TYPE,
          ExtendedType.FLOAT));
    }
    // -----------------------------------------------------------------------------
    final String[] opstrigo = { "COS", "SIN", "TAN", "ACOS", "ASIN", "ATAN" };
    for (String op : opstrigo) {
      registerSafe(new UnaryArithmeticOperatorDefintion(op, "com.squid.domain.opertor.extension." + op, OperatorDefinition.PREFIX_POSITION, op, IDomain.CONTINUOUS, OperatorDefinition.TRIGO_TYPE,
          ExtendedType.FLOAT));
    }
    // -----------------------------------------------------------------------------
    registerSafe(new VectorFriendlyOperatorDefinition("MAX", MAX));
    registerSafe(new VectorFriendlyOperatorDefinition("MIN", MIN));
    registerSafe(new VectorFriendlyOperatorDefinition("AVG", AVG));
    registerSafe(new VectorFriendlyOperatorDefinition("STDDEV", STDDEV));
    registerSafe(new OrderedAnalyticOperatorDefinition("SUM", SUM));
    registerSafe(new VarianceOperatorDefinition("VARIANCE", VARIANCE));
    registerSafe(new CoVarPopOperatorDefinition("COVAR_POP", COVAR_POP));
    registerSafe(new VarSampOperatorDefinition("VAR_SAMP", VAR_SAMP));
    registerSafe(new StdevPopOperatorDefinition("STDDEV_POP", STDDEV_POP));
    registerSafe(new StdevSampOperatorDefinition("STDDEV_SAMP", STDDEV_SAMP));
    registerSafe(new PercentileOperatorDefinition("PERCENTILE", PERCENTILE));
    registerSafe(new AggregateOperatorDefinition("MEDIAN", MEDIAN));
    registerSafe(new RankOperatorDefinition("RANK", RankOperatorDefinition.RANK_ID));
    registerSafe(new RankOperatorDefinition("ROW_NUMBER", RankOperatorDefinition.ROWNUMBER_ID));
    registerSafe(new CountOperatorDefinition("COUNT", COUNT));
    registerSafe(new CountDistinctOperatorDefinition("COUNT_DISTINCT", COUNT_DISTINCT));
    registerSafe(new CountDistinctOperatorDefinition("DISTINCT", COUNT_DISTINCT));// short-hand
    // -----------------------------------------------------------------------------
    registerSafe(new ComparisonOperatorDefinition("LESS", LESS, "<"));
    registerSafe(new ComparisonOperatorDefinition("LESS_OR_EQUAL", LESS_OR_EQUAL, "<="));
    registerSafe(new ComparisonOperatorDefinition("GREATER", GREATER, ">"));
    registerSafe(new ComparisonOperatorDefinition("GREATER_OR_EQUAL", GREATER_OR_EQUAL, ">="));
    registerSafe(new ComparisonOperatorDefinition("EQUAL", EQUAL, "="));
    registerSafe(new ComparisonOperatorDefinition("NOT_EQUAL", NOT_EQUAL, "!="));
    registerSafe(new InOperatorDefinition("IN", IN, "IN"));
    registerSafe(new ConditionalOperatorDefinition("ISNULL", ISNULL));
    registerSafe(new ConditionalOperatorDefinition("ISNOTNULL", IS_NOTNULL));
    registerSafe(new ComparisonOperatorDefinition("LIKE", LIKE, "LIKE") {
      @Override
      protected String getPrettyPrintSymbol() {
        return " " + getSymbol() + " ";
      }
    });
    registerSafe(new ComparisonOperatorDefinition("RLIKE", RLIKE, "RLIKE") {
      @Override
      protected String getPrettyPrintSymbol() {
        return " " + getSymbol() + " ";
      }
    });
    // -----------------------------------------------------------------------------
    registerSafe(new UnaryLogicalOperatorDefinition("NOT", NOT));
    registerSafe(new OrAndConditionalOperatorDefinition("OR", OR, "||"));
    registerSafe(new OrAndConditionalOperatorDefinition("AND", AND, "&&"));
    // -----------------------------------------------------------------------------
    registerSafe(new ExistsOperatorDefinition("EXISTS", EXISTS));
    // -----------------------------------------------------------------------------
    registerSafe(new CaseOperatorDefinition("CASE", CASE));
    // -----------------------------------------------------------------------------
    registerSafe(new GroupOperatorDefintion());
    // -----------------------------------------------------------------------------
    registerSafe(new CoalesceOperatorDefinition("COALESCE", COALESCE));
    // -----------------------------------------------------------------------------
    registerSafe(new VectorOperatorDefinition());

  }

  /**
   * Lookup an operator by its extendedID
   *
   * @param extendedID
   * @return
   */
  public OperatorDefinition lookupByExtendedID(String extendedID) {
    return lookupByExtendedID.get(extendedID);
  }

  /**
   * lookup OperatorDefinition by ID
   *
   * @param id
   * @return the OperatorDefinition or null if ID not defined
   * @throws ScopeException
   *         if not exception
   */
  public OperatorDefinition lookupByID(int id) {
    if (id == IntrinsicOperators.EXTENDED_ID) {
      return null;// cannot lookup an extended operator by ID
    }
    int pos = id - IntrinsicOperators.START_ID;
    if (pos < 0 || pos >= m_lookupByID.size()) {
      // throw new RuntimeException("function not defined");
      return undefinedOperatorDef;
    } else {
      return m_lookupByID.get(pos);
    }
  }

  /**
   * @param name
   * @throws ScopeException
   */
  public OperatorDefinition lookupByName(String name) throws ScopeException {
    OperatorDefinition def = m_lookupByName.get(name);
    if (def == null) {
      // T471: no more handling undefined function - it's a nightmare for the user, and it's not really used
      throw new ScopeException("unknown function '" + name + "'");
    } else {
      return def;
    }
  }

  public Set<OperatorDefinition> looseLookupByName(String name) throws ScopeException {
    Set<String> set = m_lookupByName.keySet();
    name.replaceAll("'","");
    Set<OperatorDefinition> proposal = new HashSet<OperatorDefinition>();
    for(String func : set){
      if(func.startsWith(name)){
        proposal.add(m_lookupByName.get(func)); //take the first one for now
        //return  m_lookupByName.get(func);
      }
    }
    if(proposal.size()==0){
      throw new ScopeException("unknown function '" + name + "'");
    }else{
      return proposal;
    }
  }


  /**
   * @param fun
   */
  public int lookupIDbyName(String name) {
    OperatorDefinition def = m_lookupByName.get(name);
    if (def == null) {
      throw new RuntimeException("function '" + name + "' is not defined");
    } else {
      return def.getId();
    }
  }

  public void registerExtension(OperatorDefinition def) throws OperatorScopeException {
    registerInternal(def);
  }

  public void registerLegacy(OperatorDefinition def, int ID) throws OperatorScopeException {
    if (m_lookupByName.get(def.getName()) == null) {
      m_lookupByName.put(def.getName(), def);
    }
    //
    // manage legacy ID
    int pos = ID - IntrinsicOperators.START_ID;
    if (pos >= m_lookupByID.size()) {
      m_lookupByID.setSize(pos + 1);
    }
    m_lookupByID.set(pos, def);
    //
    // manage extendedID
    String extendedID = IntrinsicOperators.INTRINSIC_EXTENDED_ID + "." + ID;
    lookupByExtendedID.put(extendedID, def);
    //
  }

  /**
   * register an Operator definition without throwing any exception. Return the opDef so you can use lisp like syntax...
   *
   * @param def
   * @return
   */
  private OperatorDefinition registerSafe(OperatorDefinition def) {
    try {
      registerInternal(def);
    } catch (OperatorScopeException e) {
      e.printStackTrace();
    }
    //
    return def;
  }

  /**
   * @param string
   * @param max
   * @throws OperatorScopeException
   */
  private void registerInternal(OperatorDefinition def) throws OperatorScopeException {
    m_lookupByName.put(def.getName(), def);
    //
    // manage legacy ID
    if (def.getId() != IntrinsicOperators.EXTENDED_ID) {
      int pos = def.getId() - IntrinsicOperators.START_ID;
      if (pos >= m_lookupByID.size()) {
        m_lookupByID.setSize(pos + 1);
      }
      m_lookupByID.set(pos, def);
    }
    //
    // manage extendedID
    if (lookupByExtendedID.contains(def.getExtendedID())) {
      throw new OperatorScopeException("ExtendedID already used: '" + def.getExtendedID() + "'");
    }
    lookupByExtendedID.put(def.getExtendedID(), def);
    //
    switch (def.getType()) {
      case OperatorDefinition.ALGEBRAIC_TYPE:
        m_algebraicList.add(def);
        break;
      case OperatorDefinition.AGGREGATE_TYPE:
        m_aggregateList.add(def);
        break;
    }
  }

  /**
   * @param type
   *        is either ALGEBRAIC_TYPE or AGGREGATE_TYPE
   * @return
   */
  public List<OperatorDefinition> getOperators(int type) {
    switch (type) {
      case OperatorDefinition.ALGEBRAIC_TYPE:
        return m_algebraicList;
      case OperatorDefinition.AGGREGATE_TYPE:
        return m_aggregateList;
      default:
        return Collections.emptyList();
    }
  }

  public Collection<OperatorDefinition> getRegisteredOperators() {
    return Collections.unmodifiableCollection(m_lookupByName.values());
  }

}
