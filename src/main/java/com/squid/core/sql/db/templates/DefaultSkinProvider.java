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
package com.squid.core.sql.db.templates;

import java.util.List;

import com.squid.core.database.model.DatabaseProduct;
import com.squid.core.domain.aggregate.GroupingIDOperatorDefinition;
import com.squid.core.domain.aggregate.GroupingOperatorDefinition;
import com.squid.core.domain.aggregate.QuotientOperatorDefinition;
import com.squid.core.domain.analytics.WindowingOperatorRegistry;
import com.squid.core.domain.extensions.JSON.JSONArrayLengthOperatorDefinition;
import com.squid.core.domain.extensions.JSON.JSONExtractArrayElementTextOperatorDefinition;
import com.squid.core.domain.extensions.JSON.JSONExtractPathTextOperatorDefinition;
import com.squid.core.domain.extensions.cast.CastOperatorDefinition;
import com.squid.core.domain.extensions.date.AddMonthsOperatorDefinition;
import com.squid.core.domain.extensions.date.DateTruncateOperatorDefinition;
import com.squid.core.domain.extensions.date.DateTruncateShortcutsOperatorDefinition;
import com.squid.core.domain.extensions.date.extract.ExtractOperatorDefinition;
import com.squid.core.domain.extensions.date.operator.DateMonthsBetweenOperatorDefinition;
import com.squid.core.domain.extensions.date.operator.DateOperatorDefinition;
import com.squid.core.domain.extensions.string.PosStringOperatorDefinition;
import com.squid.core.domain.extensions.string.SplitPartOperatorDefinition;
import com.squid.core.domain.extensions.string.StringLengthOperatorsDefinition;
import com.squid.core.domain.extensions.string.SubstringOperatorDefinition;
import com.squid.core.domain.extensions.string.oneArgStringOperator.OneArgStringOperatorDefinition;
import com.squid.core.domain.extensions.string.pad.PadOperatorDefinition;
import com.squid.core.domain.extensions.string.regex.RegexpOperatorDefinition;
import com.squid.core.domain.extensions.string.translate.TranslateOperatorDefinition;
import com.squid.core.domain.extensions.string.trim.TrimOperatorDefinition;
import com.squid.core.domain.maths.CeilOperatorDefinition;
import com.squid.core.domain.maths.DegreesOperatorDefintion;
import com.squid.core.domain.maths.FloorOperatorDefinition;
import com.squid.core.domain.maths.GreatestLeastOperatorDefinition;
import com.squid.core.domain.maths.PiOperatorDefinition;
import com.squid.core.domain.maths.PowerOperatorDefinition;
import com.squid.core.domain.maths.RadiansOperatorDefintion;
import com.squid.core.domain.maths.RandOperatorDefinition;
import com.squid.core.domain.maths.RoundOperatorDefinition;
import com.squid.core.domain.maths.SignOperatorDefinition;
import com.squid.core.domain.maths.SinhCoshTanhOperatorDefinition;
import com.squid.core.domain.maths.TruncateOperatorDefinition;
import com.squid.core.domain.operators.CompareToOperatorDefinition;
import com.squid.core.domain.operators.IntrinsicOperators;
import com.squid.core.domain.operators.OperatorDefinition;
import com.squid.core.domain.operators.OperatorScope;
import com.squid.core.domain.operators.RankOperatorDefinition;
import com.squid.core.domain.sort.SortOperatorDefinition;
import com.squid.core.domain.stats.PercentileOperatorDefintion;
import com.squid.core.domain.vector.VectorOperatorDefinition;
import com.squid.core.sql.db.features.IRollupStrategySupport;
import com.squid.core.sql.db.render.AddMonthsOperatorRenderer;
import com.squid.core.sql.db.render.AndOperatorRenderer;
import com.squid.core.sql.db.render.AverageOperatorRenderer;
import com.squid.core.sql.db.render.CaseOperatorRender;
import com.squid.core.sql.db.render.CastOperatorRenderer;
import com.squid.core.sql.db.render.CeilOperatorRenderer;
import com.squid.core.sql.db.render.CoVarianceRenderer;
import com.squid.core.sql.db.render.CoalesceOperatorRenderer;
import com.squid.core.sql.db.render.CountDistinctOperatorRenderer;
import com.squid.core.sql.db.render.CountOperatorRenderer;
import com.squid.core.sql.db.render.CurrentDateTimestampRenderer;
import com.squid.core.sql.db.render.DateAddOperatorRenderer;
import com.squid.core.sql.db.render.DateEpochOperatorRenderer;
import com.squid.core.sql.db.render.DateIntervalOperatorRenderer;
import com.squid.core.sql.db.render.DateSubOperatorRenderer;
import com.squid.core.sql.db.render.DateTruncateOperatorRenderer;
import com.squid.core.sql.db.render.DefaultOperatorRenderer;
import com.squid.core.sql.db.render.DegreesOperatorRenderer;
import com.squid.core.sql.db.render.DivideOperatorRenderer;
import com.squid.core.sql.db.render.ExtractOperatorRenderer;
import com.squid.core.sql.db.render.FloorOperatorRenderer;
import com.squid.core.sql.db.render.GreatestLeastOperatorRenderer;
import com.squid.core.sql.db.render.InOperatorRenderer;
import com.squid.core.sql.db.render.IsNotNullOperatorRenderer;
import com.squid.core.sql.db.render.IsNullOperatorRenderer;
import com.squid.core.sql.db.render.LikeOperatorRenderer;
import com.squid.core.sql.db.render.MinMaxOperatorRenderer;
import com.squid.core.sql.db.render.NullIfOperatorRenderer;
import com.squid.core.sql.db.render.OperatorRenderer;
import com.squid.core.sql.db.render.OrOperatorRenderer;
import com.squid.core.sql.db.render.PadOperatorRenderer;
import com.squid.core.sql.db.render.PiOperatorRenderer;
import com.squid.core.sql.db.render.PowerOperatorRenderer;
import com.squid.core.sql.db.render.QuotientOperatorRenderer;
import com.squid.core.sql.db.render.RLikeOperatorRenderer;
import com.squid.core.sql.db.render.RankOperatorRenderer;
import com.squid.core.sql.db.render.RegexpOperatorRenderer;
import com.squid.core.sql.db.render.RoundOperatorRenderer;
import com.squid.core.sql.db.render.RowsOperatorRenderer;
import com.squid.core.sql.db.render.SignOperatorRenderer;
import com.squid.core.sql.db.render.SinhCoshTanhOperatorRenderer;
import com.squid.core.sql.db.render.SortOperatorRenderer;
import com.squid.core.sql.db.render.StringLengthRenderer;
import com.squid.core.sql.db.render.StringOneArgOperatorRenderer;
import com.squid.core.sql.db.render.SubstractionOperatorRenderer;
import com.squid.core.sql.db.render.ThreeArgsFunctionRenderer;
import com.squid.core.sql.db.render.TrimOperatorRenderer;
import com.squid.core.sql.db.render.TruncateOperatorRenderer;
import com.squid.core.sql.render.ISkinFeatureSupport;
import com.squid.core.sql.render.SQLSkin;

/**
 * The default skin provider
 *
 * @author serge fantino
 *
 */
public class DefaultSkinProvider implements ISkinProvider {

	private DelegateOperatorRendererRegistry delegateRendererRegistry;

	public DefaultSkinProvider() {
		delegateRendererRegistry = new DelegateOperatorRendererRegistry();
		//

		// INSTRISTIC OPERATOR
		registerOperatorRender(OperatorDefinition.getExtendedId(IntrinsicOperators.ABS), new DefaultOperatorRenderer() );
		registerOperatorRender(OperatorDefinition.getExtendedId(IntrinsicOperators.ADD_MONTHS), new DefaultOperatorRenderer() );
		registerOperatorRender(OperatorDefinition.getExtendedId(IntrinsicOperators.AND), new AndOperatorRenderer() );
		registerOperatorRender(OperatorDefinition.getExtendedId(IntrinsicOperators.CONCAT), new DefaultOperatorRenderer() );
		registerOperatorRender(OperatorDefinition.getExtendedId(IntrinsicOperators.COUNT_DISTINCT), new CountDistinctOperatorRenderer() );
		registerOperatorRender(OperatorDefinition.getExtendedId(IntrinsicOperators.DIVIDE), new DivideOperatorRenderer() );
		registerOperatorRender(OperatorDefinition.getExtendedId(IntrinsicOperators.EQUAL), new DefaultOperatorRenderer() );
		registerOperatorRender(OperatorDefinition.getExtendedId(IntrinsicOperators.EXISTS), new DefaultOperatorRenderer() );
		registerOperatorRender(OperatorDefinition.getExtendedId(IntrinsicOperators.EXPONENTIATE), new DefaultOperatorRenderer() );
		registerOperatorRender(OperatorDefinition.getExtendedId(IntrinsicOperators.GREATER), new DefaultOperatorRenderer() );
		registerOperatorRender(OperatorDefinition.getExtendedId(IntrinsicOperators.GREATER_OR_EQUAL), new DefaultOperatorRenderer() );
		registerOperatorRender(OperatorDefinition.getExtendedId(IntrinsicOperators.IS_NOTNULL), new IsNotNullOperatorRenderer() );
		registerOperatorRender(OperatorDefinition.getExtendedId(IntrinsicOperators.ISNULL), new IsNullOperatorRenderer() );
		registerOperatorRender(OperatorDefinition.getExtendedId(IntrinsicOperators.LESS), new DefaultOperatorRenderer() );
		registerOperatorRender(OperatorDefinition.getExtendedId(IntrinsicOperators.LESS_OR_EQUAL), new DefaultOperatorRenderer() );
		registerOperatorRender(OperatorDefinition.getExtendedId(IntrinsicOperators.LIKE), new LikeOperatorRenderer() );
		registerOperatorRender(OperatorDefinition.getExtendedId(IntrinsicOperators.MEDIAN), new DefaultOperatorRenderer() );
		registerOperatorRender(OperatorDefinition.getExtendedId(IntrinsicOperators.MINUS), new DefaultOperatorRenderer() );
		registerOperatorRender(OperatorDefinition.getExtendedId(IntrinsicOperators.MODULO), new DefaultOperatorRenderer() );
		registerOperatorRender(OperatorDefinition.getExtendedId(IntrinsicOperators.MULTIPLY), new DefaultOperatorRenderer() );
		registerOperatorRender(OperatorDefinition.getExtendedId(IntrinsicOperators.NOT), new DefaultOperatorRenderer() );
		registerOperatorRender(OperatorDefinition.getExtendedId(IntrinsicOperators.NOT_EQUAL), new DefaultOperatorRenderer() );
		registerOperatorRender(OperatorDefinition.getExtendedId(IntrinsicOperators.NULLIF), new NullIfOperatorRenderer() );
		registerOperatorRender(OperatorDefinition.getExtendedId(IntrinsicOperators.OR), new OrOperatorRenderer() );
		registerOperatorRender(OperatorDefinition.getExtendedId(IntrinsicOperators.PLUS), new DefaultOperatorRenderer() );
		registerOperatorRender(OperatorDefinition.getExtendedId(IntrinsicOperators.STDDEV_POP), new DefaultOperatorRenderer() );
		registerOperatorRender(OperatorDefinition.getExtendedId(IntrinsicOperators.STDDEV_SAMP), new DefaultOperatorRenderer() );
		registerOperatorRender(OperatorDefinition.getExtendedId(IntrinsicOperators.SUBTRACTION), new SubstractionOperatorRenderer() );
		registerOperatorRender(OperatorDefinition.getExtendedId(IntrinsicOperators.SUM), new DefaultOperatorRenderer() );
		registerOperatorRender(OperatorDefinition.getExtendedId(IntrinsicOperators.VAR_SAMP), new DefaultOperatorRenderer() );
		registerOperatorRender(OperatorDefinition.getExtendedId(IntrinsicOperators.VAR_POP), new DefaultOperatorRenderer() );
		registerOperatorRender(OperatorDefinition.getExtendedId(IntrinsicOperators.COVAR_POP),new CoVarianceRenderer());
		registerOperatorRender(OperatorDefinition.getExtendedId(IntrinsicOperators.IDENTITY), new DefaultOperatorRenderer() );

		final String[] ops = { "EXP", "LN", "LOG", "SQRT" };
		for (String op : ops) {
			registerOperatorRender("com.squid.domain.operators.extension." + op, new DefaultOperatorRenderer() );
		}
		final String[] opstrigo = { "COS", "SIN", "TAN", "ACOS", "ASIN", "ATAN" };
		for (String op : opstrigo) {
			registerOperatorRender("com.squid.domain.operators.extension." + op, new DefaultOperatorRenderer() );
		}

		registerOperatorRender(RadiansOperatorDefintion.RADIANS, new DefaultOperatorRenderer());

		registerOperatorRender(RankOperatorDefinition.RANK_ID, new RankOperatorRenderer() );
		registerOperatorRender(RandOperatorDefinition.RAND, new DefaultOperatorRenderer() );
		registerOperatorRender(RankOperatorDefinition.ROWNUMBER_ID, new DefaultOperatorRenderer() );

		registerOperatorRender(	JSONExtractPathTextOperatorDefinition.ID, new DefaultOperatorRenderer() );
		registerOperatorRender(JSONArrayLengthOperatorDefinition.ID, new DefaultOperatorRenderer() );
		registerOperatorRender(JSONExtractArrayElementTextOperatorDefinition.ID, new DefaultOperatorRenderer() );

		registerOperatorRender(RegexpOperatorDefinition.REGEXP_COUNT, new DefaultOperatorRenderer() );
		registerOperatorRender(RegexpOperatorDefinition.REGEXP_INSTR, new DefaultOperatorRenderer() );

		registerOperatorRender(CompareToOperatorDefinition.GROWTH,new DefaultOperatorRenderer() );
		registerOperatorRender(CompareToOperatorDefinition.COMPARE_TO,new DefaultOperatorRenderer() );
		registerOperatorRender(OperatorDefinition.getExtendedId(IntrinsicOperators.COALESCE) ,new CoalesceOperatorRenderer() );
		registerOperatorRender(OperatorDefinition.getExtendedId(IntrinsicOperators.RLIKE), new RLikeOperatorRenderer("~") );
		registerOperatorRender(PercentileOperatorDefintion.PERCENTILE, new DefaultOperatorRenderer() );
		registerOperatorRender(OperatorDefinition.getExtendedId( IntrinsicOperators.UNDEFINED_ID),new DefaultOperatorRenderer() );

		registerOperatorRender(WindowingOperatorRegistry.WINDOWING_FOLLOWING_ID,new DefaultOperatorRenderer() );
		registerOperatorRender(WindowingOperatorRegistry.WINDOWING_UNBOUNDED_ID,new DefaultOperatorRenderer() );
		registerOperatorRender(WindowingOperatorRegistry.WINDOWING_PRECEDING_ID,new DefaultOperatorRenderer() );
		registerOperatorRender(WindowingOperatorRegistry.WINDOWING_CURRENT_ID,new DefaultOperatorRenderer() );


		registerOperatorRender(SubstringOperatorDefinition.STRING_SUBSTRING,new DefaultOperatorRenderer() );
		registerOperatorRender(PosStringOperatorDefinition.STRING_POSITION,new DefaultOperatorRenderer() );
		registerOperatorRender(VectorOperatorDefinition.ID,new DefaultOperatorRenderer() );

		registerOperatorRender(OperatorDefinition.getExtendedId(GroupingIDOperatorDefinition.ID),new DefaultOperatorRenderer() );
		registerOperatorRender(GroupingOperatorDefinition.ID,new DefaultOperatorRenderer() );

		registerOperatorRender(DateMonthsBetweenOperatorDefinition.ID,new DefaultOperatorRenderer() );


		registerOperatorRender(OperatorScope.getDefault().lookupByID(OperatorScope.COUNT).getExtendedID(),
				new CountOperatorRenderer());
		//
		registerOperatorRender(AddMonthsOperatorDefinition.ADD_MONTHS, new AddMonthsOperatorRenderer());
		//
		registerOperatorRender(DateOperatorDefinition.DATE_ADD, new DateAddOperatorRenderer());
		registerOperatorRender(DateOperatorDefinition.DATE_SUB, new DateSubOperatorRenderer());
		registerOperatorRender(DateOperatorDefinition.DATE_INTERVAL, new DateIntervalOperatorRenderer());
		registerOperatorRender(DateOperatorDefinition.FROM_UNIXTIME,
				new DateEpochOperatorRenderer(DateEpochOperatorRenderer.FROM));
		registerOperatorRender(DateOperatorDefinition.TO_UNIXTIME,
				new DateEpochOperatorRenderer(DateEpochOperatorRenderer.TO));
		registerOperatorRender(DateTruncateOperatorDefinition.DATE_TRUNCATE, new DateTruncateOperatorRenderer());
		registerOperatorRender(DateTruncateShortcutsOperatorDefinition.HOURLY_ID, new DateTruncateOperatorRenderer());
		registerOperatorRender(DateTruncateShortcutsOperatorDefinition.DAILY_ID, new DateTruncateOperatorRenderer());
		registerOperatorRender(DateTruncateShortcutsOperatorDefinition.WEEKLY_ID, new DateTruncateOperatorRenderer());
		registerOperatorRender(DateTruncateShortcutsOperatorDefinition.QUARTERLY_ID, new DateTruncateOperatorRenderer());
		registerOperatorRender(DateTruncateShortcutsOperatorDefinition.MONTHLY_ID, new DateTruncateOperatorRenderer());
		registerOperatorRender(DateTruncateShortcutsOperatorDefinition.YEARLY_ID, new DateTruncateOperatorRenderer());
		//
		registerOperatorRender(ExtractOperatorDefinition.EXTRACT_DAY, new ExtractOperatorRenderer("DAY"));
		registerOperatorRender(ExtractOperatorDefinition.EXTRACT_DAY_OF_WEEK,
				new ExtractOperatorRenderer("DAY_OF_WEEK"));
		registerOperatorRender(ExtractOperatorDefinition.EXTRACT_DAY_OF_YEAR,
				new ExtractOperatorRenderer("DAY_OF_YEAR"));
		registerOperatorRender(ExtractOperatorDefinition.EXTRACT_MONTH, new ExtractOperatorRenderer("MONTH"));
		registerOperatorRender(ExtractOperatorDefinition.EXTRACT_YEAR, new ExtractOperatorRenderer("YEAR"));
		registerOperatorRender(ExtractOperatorDefinition.EXTRACT_HOUR, new ExtractOperatorRenderer("HOUR"));
		registerOperatorRender(ExtractOperatorDefinition.EXTRACT_MINUTE, new ExtractOperatorRenderer("MINUTE"));
		registerOperatorRender(ExtractOperatorDefinition.EXTRACT_SECOND, new ExtractOperatorRenderer("SECOND"));
		//
		// See Ticket #1620
		// registerOperatorRender(IntervalOperatorDefinition.INTERVAL_DAY, new
		// IntervalOperatorRenderer("DAY"));
		// registerOperatorRender(IntervalOperatorDefinition.INTERVAL_MONTH, new
		// IntervalOperatorRenderer("MONTH"));
		// registerOperatorRender(IntervalOperatorDefinition.INTERVAL_YEAR, new
		// IntervalOperatorRenderer("YEAR"));
		// registerOperatorRender(IntervalOperatorDefinition.INTERVAL_HOUR, new
		// IntervalOperatorRenderer("HOUR"));
		// registerOperatorRender(IntervalOperatorDefinition.INTERVAL_MINUTE,
		// new IntervalOperatorRenderer("MINUTE"));
		// registerOperatorRender(IntervalOperatorDefinition.INTERVAL_SECOND,
		// new IntervalOperatorRenderer("SECOND"));
		//
		registerOperatorRender(OperatorDefinition.getExtendedId(IntrinsicOperators.CASE), new CaseOperatorRender());
		registerOperatorRender(OperatorDefinition.getExtendedId(IntrinsicOperators.NULLIF), new NullIfOperatorRenderer());
		//
		registerOperatorRender(StringLengthOperatorsDefinition.STRING_LENGTH, new StringLengthRenderer());
		registerOperatorRender(TranslateOperatorDefinition.STRING_REPLACE, new ThreeArgsFunctionRenderer("REPLACE"));
		registerOperatorRender(TranslateOperatorDefinition.STRING_TRANSLATE,
				new ThreeArgsFunctionRenderer("TRANSLATE"));
		registerOperatorRender(OneArgStringOperatorDefinition.STRING_UPPER, new StringOneArgOperatorRenderer("UPPER"));
		registerOperatorRender(OneArgStringOperatorDefinition.STRING_LOWER, new StringOneArgOperatorRenderer("LOWER"));
		registerOperatorRender(OneArgStringOperatorDefinition.STRING_REVERSE,
				new StringOneArgOperatorRenderer("REVERSE"));
		registerOperatorRender(OneArgStringOperatorDefinition.STRING_MD5, new StringOneArgOperatorRenderer("MD5"));
		registerOperatorRender(SplitPartOperatorDefinition.STRING_SPLIT_PART,
				new ThreeArgsFunctionRenderer("SPLIT_PART"));
		registerOperatorRender(TrimOperatorDefinition.STRING_TRIM, new TrimOperatorRenderer("BOTH"));
		registerOperatorRender(TrimOperatorDefinition.STRING_LTRIM, new TrimOperatorRenderer("LEADING"));
		registerOperatorRender(TrimOperatorDefinition.STRING_RTRIM, new TrimOperatorRenderer("TRAILING"));
		registerOperatorRender(PadOperatorDefinition.STRING_LPAD, new PadOperatorRenderer("LPAD"));
		registerOperatorRender(PadOperatorDefinition.STRING_RPAD, new PadOperatorRenderer("RPAD"));
		//
		registerOperatorRender(RegexpOperatorDefinition.REGEXP_REPLACE, new RegexpOperatorRenderer("REGEXP_REPLACE"));
		registerOperatorRender(RegexpOperatorDefinition.REGEXP_SUBSTR, new RegexpOperatorRenderer("REGEXP_SUBSTR"));
		//
		registerOperatorRender(SortOperatorDefinition.ASC_ID, new SortOperatorRenderer("ASC"));
		registerOperatorRender(SortOperatorDefinition.DESC_ID, new SortOperatorRenderer("DESC"));
		registerOperatorRender(DateOperatorDefinition.CURRENT_DATE, new CurrentDateTimestampRenderer());
		registerOperatorRender(DateOperatorDefinition.CURRENT_TIMESTAMP, new CurrentDateTimestampRenderer());
		registerOperatorRender(CastOperatorDefinition.TO_CHAR, new CastOperatorRenderer());
		registerOperatorRender(CastOperatorDefinition.TO_DATE, new CastOperatorRenderer());
		registerOperatorRender(CastOperatorDefinition.TO_INTEGER, new CastOperatorRenderer());
		registerOperatorRender(CastOperatorDefinition.TO_NUMBER, new CastOperatorRenderer());
		registerOperatorRender(CastOperatorDefinition.TO_TIMESTAMP, new CastOperatorRenderer());
		registerOperatorRender(CeilOperatorDefinition.CEIL, new CeilOperatorRenderer());
		registerOperatorRender(FloorOperatorDefinition.FLOOR, new FloorOperatorRenderer());
		registerOperatorRender(SignOperatorDefinition.SIGN, new SignOperatorRenderer());
		registerOperatorRender(TruncateOperatorDefinition.TRUNCATE, new TruncateOperatorRenderer());
		registerOperatorRender(RoundOperatorDefinition.ROUND, new RoundOperatorRenderer());
		registerOperatorRender(PowerOperatorDefinition.POWER, new PowerOperatorRenderer());
		registerOperatorRender(PiOperatorDefinition.PI, new PiOperatorRenderer());
		registerOperatorRender(DegreesOperatorDefintion.DEGREES, new DegreesOperatorRenderer());
		registerOperatorRender(SinhCoshTanhOperatorDefinition.SINH, new SinhCoshTanhOperatorRenderer("SINH"));
		registerOperatorRender(SinhCoshTanhOperatorDefinition.COSH, new SinhCoshTanhOperatorRenderer("COSH"));
		registerOperatorRender(SinhCoshTanhOperatorDefinition.TANH, new SinhCoshTanhOperatorRenderer("TANH"));
		//
		registerOperatorRender(OperatorDefinition.getExtendedId(IntrinsicOperators.AVG), new AverageOperatorRenderer());
		registerOperatorRender(OperatorDefinition.getExtendedId(IntrinsicOperators.MIN), new MinMaxOperatorRenderer());
		registerOperatorRender(OperatorDefinition.getExtendedId(IntrinsicOperators.MAX), new MinMaxOperatorRenderer());

		//JTH 20170519: STDDEV is STDDEV_SAMP & not supported as ordered analytical function, so remove it
		//registerOperatorRender(OperatorDefinition.getExtendedId(IntrinsicOperators.STDDEV),new StddevOperatorRenderer());

		// VECTOR SUPPORT
		registerOperatorRender(OperatorDefinition.getExtendedId(IntrinsicOperators.IN), new InOperatorRenderer());
		//
		// default support for LEAST and GREATEST
		registerOperatorRender(GreatestLeastOperatorDefinition.LEAST, new GreatestLeastOperatorRenderer());
		registerOperatorRender(GreatestLeastOperatorDefinition.GREATEST, new GreatestLeastOperatorRenderer());
		//
		// proto: QUOTIENT operator
		registerOperatorRender(QuotientOperatorDefinition.ID, new QuotientOperatorRenderer());
		//
		registerOperatorRender(WindowingOperatorRegistry.WINDOWING_ROWS_ID, new RowsOperatorRenderer());
	}

	@Override
	public DelegateOperatorRendererRegistry getDelegateRendererRegistry() {
		return delegateRendererRegistry;
	}

	/**
	 * register an OperatorRenderer for an operator extendedID
	 *
	 * @param extract_day
	 * @param renderer
	 */
	protected void registerOperatorRender(String extendedID, OperatorRenderer renderer) {
		delegateRendererRegistry.registerOperatorRender(extendedID, renderer);
	}

	protected void unregisterOperatorRender(String extendedID) {
		delegateRendererRegistry.unregisterOperatorRender(extendedID);
	}

	@Override
	public double computeAccuracy(DatabaseProduct product) {
		/**
		 * the accuracy of the default provider is the lowest acceptable
		 * possible
		 */
		return LOWEST_APPLICABLE;
	}

	@Override
	public SQLSkin createSkin(DatabaseProduct product) {
		return new DefaultJDBCSkin(this, product);
	}

	@Override
	public boolean canRender(String extendedID) {
		return delegateRendererRegistry.canRender(extendedID);
	}

	@Override
	public List<String> canRender() {
		return delegateRendererRegistry.canRender();
	}

	@Override
	public ISkinFeatureSupport getFeatureSupport(DefaultJDBCSkin skin, String featureID) {
		if (featureID.equals(IRollupStrategySupport.ID)) {
			return IRollupStrategySupport.DO_NOT_OPTIMIZE_STRATEGY;
		}
		return ISkinFeatureSupport.IS_NOT_SUPPORTED;
	}

	@Override
	public String getSkinPrefix(DatabaseProduct product) {
		return "default";
	}

	@Override
	public ISkinProvider getParentSkinProvider() {
		return null;
	}

}
