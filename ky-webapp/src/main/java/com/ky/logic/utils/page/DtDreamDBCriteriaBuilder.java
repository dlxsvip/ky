package com.ky.logic.utils.page;

import org.hibernate.Criteria;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.hibernate.persister.collection.CollectionPropertyNames;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ZY on 2016/2/1.
 */
public class DtDreamDBCriteriaBuilder {

    private String orderByAli;
    private String orderBy;
    private Criteria aliCr;
    private Criteria openstackCr;
    private boolean orderAscending;
    private boolean resultTransformer;
    private List<Parameter> aliFilterParams = new ArrayList<Parameter>();
    private List<Parameter> openstackFilterParams = new ArrayList<Parameter>();

    public List<Parameter> getAliFilterParams() {
        return aliFilterParams;
    }

    public void setAliFilterParams(List<Parameter> aliFilterParams) {
        this.aliFilterParams = aliFilterParams;
    }

    public List<Parameter> getOpenstackFilterParams() {
        return openstackFilterParams;
    }

    public void setOpenstackFilterParams(List<Parameter> openstackFilterParams) {
        this.openstackFilterParams = openstackFilterParams;
    }

    public Criteria getAliCr() {
        return aliCr;
    }

    public void setAliCr(Criteria aliCr) {
        this.aliCr = aliCr;
    }

    public String getOrderByAli() {
        return orderByAli;
    }

    public void setOrderByAli(String orderByAli) {
        this.orderByAli = orderByAli;
    }

    public Criteria getOpenstackCr() {
        return openstackCr;
    }

    public void setOpenstackCr(Criteria openstackCr) {
        this.openstackCr = openstackCr;
    }

    public String getOrderBy() {
        return orderBy;
    }

    public void setOrderBy(String orderBy) {
        this.orderBy = orderBy;
    }

    public boolean getOrderAscending() {
        return orderAscending;
    }

    public void setOrderAscending(boolean orderAscending) {
        this.orderAscending = orderAscending;
    }

    public List<Parameter> getFilterParams() {
        return aliFilterParams;
    }

    public void buildAli() {
        addAliRestrictionToCriteria();

        addAliOrderToCriteria();

        addAliResultTransformerToCriteria();
    }

    public void buildOpenstack() {
        addOpenstackRestrictionToCriteria();

        addOrderToCriteria();

        addOpenstackResultTransformerToCriteria();
    }


    private void addAliResultTransformerToCriteria() {
        if (resultTransformer == true) {
            aliCr.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
        }
    }

    private void addOpenstackResultTransformerToCriteria() {
        if (resultTransformer == true) {
            openstackCr.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
        }
    }

    private Criterion parameter2Criterion(Parameter subPara) {
        Criterion subCri = null;
        if (subPara.getDtdbType() == DTDBType.ANYWHERE) {
            subCri = Restrictions.like(subPara.getName(), subPara.getValue().toString(), MatchMode.ANYWHERE);
        } else if (subPara.getDtdbType() == DTDBType.GREATER) {
            subCri = Restrictions.gt(subPara.getName(), subPara.getValue());
        } else if (subPara.getDtdbType() == DTDBType.EQUAL_GREATER) {
            subCri = Restrictions.ge(subPara.getName(), subPara.getValue());
        } else if (subPara.getDtdbType() == DTDBType.LESS) {
            subCri = Restrictions.lt(subPara.getName(), subPara.getValue());
        } else if (subPara.getDtdbType() == DTDBType.EQUAL_LESS) {
            subCri = Restrictions.le(subPara.getName(), subPara.getValue());
        } else if (subPara.getDtdbType() == DTDBType.EXACT) {
            subCri = Restrictions.eq(subPara.getName(), subPara.getValue());
        } else if (subPara.getDtdbType() == DTDBType.NEORISNOTNULL) {
            subCri = Restrictions.neOrIsNotNull(subPara.getName(), subPara.getValue());
        }

        return subCri;
    }
    private void operateListParameter(List<Criterion> criterionList1, Parameter parameter1) {
        if (parameter1.getDtdbType() == DTDBType.LIST_OR) {
            List<Parameter> parameterSubList = new ArrayList<Parameter>();
            Criterion subCri = null;
            for (Parameter subPara : parameterSubList) {
                subCri = parameter2Criterion(subPara);

                if (null != subCri) {
                    subCri = Restrictions.or(subCri);
                }
            }

            if (null != subCri) {
                criterionList1.add(subCri);
            }
        } else {
            criterionList1.add(Restrictions.in(parameter1.getName(), ((List) parameter1.getValue()).toArray()));
        }
    }
    private void addParameter(Parameter parameter, Criteria criteria) {
        if (parameter.isOrType()) {
            if (parameter.is2OrType()) {
                List<Parameter> parameterList1 = parameter.getParameterList();
                List<Parameter> parameterList2 = parameter.getParameterList2();
                List<Criterion> criterionList1 = new ArrayList<Criterion>();
                List<Criterion> criterionList2 = new ArrayList<Criterion>();
                for (Parameter parameter1 : parameterList1) {
                    if (parameter1.isValue2Null()) {
                        if (parameter1.getValue() instanceof List) {
                            operateListParameter(criterionList1, parameter1);
                        } else if (null != parameter1.getDtdbType()) {
                            if (parameter1.getDtdbType() == DTDBType.ANYWHERE) {
                                criterionList1.add(Restrictions.like(parameter1.getName(), parameter1.getValue().toString(), MatchMode.ANYWHERE));
                            } else if (parameter1.getDtdbType() == DTDBType.GREATER) {
                                criterionList1.add(Restrictions.gt(parameter1.getName(), parameter1.getValue()));
                            } else if (parameter1.getDtdbType() == DTDBType.EQUAL_GREATER) {
                                criterionList1.add(Restrictions.ge(parameter1.getName(), parameter1.getValue()));
                            } else if (parameter1.getDtdbType() == DTDBType.LESS) {
                                criterionList1.add(Restrictions.lt(parameter1.getName(), parameter1.getValue()));
                            } else if (parameter1.getDtdbType() == DTDBType.EQUAL_LESS) {
                                criterionList1.add(Restrictions.le(parameter1.getName(), parameter1.getValue()));
                            } else if (parameter1.getDtdbType() == DTDBType.EXACT) {
                                criterionList1.add(Restrictions.eq(parameter1.getName(), parameter1.getValue()));
                            } else if (parameter1.getDtdbType() == DTDBType.NEORISNOTNULL) {
                                criterionList1.add(Restrictions.neOrIsNotNull(parameter1.getName(), parameter1.getValue()));
                            }

                        } else {
                            criterionList1.add(Restrictions.eq(parameter1.getName(), parameter1.getValue()));
                        }
                    } else {
                        criterionList1.add(Restrictions.between(parameter1.getName(), parameter1.getValue(), parameter1.getValue2()));
                    }
                }

                Criterion criterion_1[] = new Criterion[criterionList1.size()];
                int index = 0;
                for (Criterion criteri : criterionList1) {
                    criterion_1[index] = criterionList1.get(index);
                    index++;
                }
                Criterion criterion1 = Restrictions.and(criterion_1);

                for (Parameter parameter1 : parameterList2) {
                    if (parameter1.isValue2Null()) {
                        if (parameter1.getValue() instanceof List) {
                            criterionList2.add(Restrictions.in(parameter1.getName(), ((List) parameter1.getValue()).toArray()));
                        } else if (null != parameter1.getDtdbType()) {
                            if (parameter1.getDtdbType() == DTDBType.ANYWHERE) {
                                criterionList2.add(Restrictions.like(parameter1.getName(), parameter1.getValue().toString(), MatchMode.ANYWHERE));
                            } else if (parameter1.getDtdbType() == DTDBType.GREATER) {
                                criterionList2.add(Restrictions.gt(parameter1.getName(), parameter1.getValue()));
                            } else if (parameter1.getDtdbType() == DTDBType.EQUAL_GREATER) {
                                criterionList2.add(Restrictions.ge(parameter1.getName(), parameter1.getValue()));
                            } else if (parameter1.getDtdbType() == DTDBType.LESS) {
                                criterionList2.add(Restrictions.lt(parameter1.getName(), parameter1.getValue()));
                            } else if (parameter1.getDtdbType() == DTDBType.EQUAL_LESS) {
                                criterionList2.add(Restrictions.le(parameter1.getName(), parameter1.getValue()));
                            } else if (parameter1.getDtdbType() == DTDBType.EXACT) {
                                criterionList2.add(Restrictions.eq(parameter1.getName(), parameter1.getValue()));
                            } else if (parameter1.getDtdbType() == DTDBType.COLLECTION_ELEMENT) {
                                criteria.createAlias(parameter1.getName(), "otherTable");
                                criterionList2.add(Restrictions.like("otherTable." + CollectionPropertyNames.COLLECTION_ELEMENTS, parameter1.getValue().toString(), MatchMode.ANYWHERE));
                            } else if (parameter1.getDtdbType() == DTDBType.NEORISNOTNULL) {
                                criterionList2.add(Restrictions.neOrIsNotNull(parameter1.getName(), parameter1.getValue()));
                            }
                        } else {
                            criterionList2.add(Restrictions.eq(parameter1.getName(), parameter1.getValue()));
                        }
                    } else {
                        criterionList2.add(Restrictions.between(parameter1.getName(), parameter1.getValue(), parameter1.getValue2()));
                    }
                }

                Criterion criterion_2[] = new Criterion[criterionList2.size()];
                index = 0;
                for (Criterion criteri : criterionList2) {
                    criterion_2[index] = criterionList2.get(index);
                    index++;
                }

                Criterion criterion2 = Restrictions.and(criterion_2);

                criteria.add(Restrictions.or(criterion1, criterion2));

            } else {
                List<Criterion> criterionList = new ArrayList<Criterion>();
                List<Parameter> parameterList = parameter.getParameterList();
                for (Parameter parameter1 : parameterList) {
                    if (parameter1.isValue2Null()) {
                        if (parameter1.getValue() instanceof List) {
                            criterionList.add(Restrictions.in(parameter1.getName(), ((List) parameter1.getValue()).toArray()));
                        }  else if (null != parameter1.getDtdbType()) {
                            if (parameter1.getDtdbType() == DTDBType.ANYWHERE) {
                                criterionList.add(Restrictions.like(parameter1.getName(), parameter1.getValue().toString(), MatchMode.ANYWHERE));
                            } else if (parameter1.getDtdbType() == DTDBType.GREATER) {
                                criterionList.add(Restrictions.gt(parameter1.getName(), parameter1.getValue()));
                            } else if (parameter1.getDtdbType() == DTDBType.EQUAL_GREATER) {
                                criterionList.add(Restrictions.ge(parameter1.getName(), parameter1.getValue()));
                            } else if (parameter1.getDtdbType() == DTDBType.LESS) {
                                criterionList.add(Restrictions.lt(parameter1.getName(), parameter1.getValue()));
                            } else if (parameter1.getDtdbType() == DTDBType.EQUAL_LESS) {
                                criterionList.add(Restrictions.le(parameter1.getName(), parameter1.getValue()));
                            } else if (parameter1.getDtdbType() == DTDBType.EXACT) {
                                criterionList.add(Restrictions.eq(parameter1.getName(), parameter1.getValue()));
                            } else if (parameter1.getDtdbType() == DTDBType.COLLECTION_ELEMENT) {
                                criteria.createAlias(parameter1.getName(), "otherTable");
                                criterionList.add(Restrictions.like("otherTable." + CollectionPropertyNames.COLLECTION_ELEMENTS, parameter1.getValue().toString(), MatchMode.ANYWHERE));
                            } else if (parameter1.getDtdbType() == DTDBType.NEORISNOTNULL) {
                                criterionList.add(Restrictions.neOrIsNotNull(parameter1.getName(), parameter1.getValue()));
                            }
                        } else {
                            criterionList.add(Restrictions.eq(parameter1.getName(), parameter1.getValue()));
                        }
                    } else {
                        criterionList.add(Restrictions.between(parameter1.getName(), parameter1.getValue(), parameter1.getValue2()));
                    }
                }
                Criterion criterion[] = new Criterion[criterionList.size()];
                int index = 0;
                for (Criterion criterion1 : criterionList) {
                    criterion[index] = criterionList.get(index);
                    index++;
                }

                criteria.add(Restrictions.or(criterion));
            }
        }

        if (!parameter.isNull() || parameter.getDtdbType() == DTDBType.NEORISNOTNULL) {
            if (parameter.isValue2Null()) {
                if (parameter.getValue() instanceof List) {
                    criteria.add(Restrictions.in(parameter.getName(), ((List) parameter.getValue()).toArray()));
                } else if (null != parameter.getDtdbType()) {
                    if (parameter.getDtdbType() == DTDBType.ANYWHERE) {
                        criteria.add(Restrictions.like(parameter.getName(), parameter.getValue().toString(), MatchMode.ANYWHERE));
                    } else if (parameter.getDtdbType() == DTDBType.GREATER) {
                        criteria.add(Restrictions.gt(parameter.getName(), parameter.getValue()));
                    } else if (parameter.getDtdbType() == DTDBType.EQUAL_GREATER) {
                        criteria.add(Restrictions.ge(parameter.getName(), parameter.getValue()));
                    } else if (parameter.getDtdbType() == DTDBType.LESS) {
                        criteria.add(Restrictions.lt(parameter.getName(), parameter.getValue()));
                    } else if (parameter.getDtdbType() == DTDBType.EQUAL_LESS) {
                        criteria.add(Restrictions.le(parameter.getName(), parameter.getValue()));
                    } else if (parameter.getDtdbType() == DTDBType.EXACT) {
                        criteria.add(Restrictions.eq(parameter.getName(), parameter.getValue()));
                    } else if (parameter.getDtdbType() == DTDBType.COLLECTION_ELEMENT) {
                        criteria.createAlias(parameter.getName(), "otherTable");
                        criteria.add(Restrictions.like("otherTable." + CollectionPropertyNames.COLLECTION_ELEMENTS, parameter.getValue().toString(), MatchMode.ANYWHERE));
                    } else if (parameter.getDtdbType() == DTDBType.NEORISNOTNULL) {
                        criteria.add(Restrictions.neOrIsNotNull(parameter.getName(), parameter.getValue()));
                    }
                } else {
                    criteria.add(Restrictions.eq(parameter.getName(), parameter.getValue()));
                }
            } else {
                if(parameter.getDtdbType() == DTDBType.RELATION_TABLE){
                    criteria.createAlias(parameter.getName(), "relationalTable");
                    criteria.add(Restrictions.like("relationalTable." + parameter.getValue().toString(), parameter.getValue2().toString(), MatchMode.ANYWHERE));
                }else {
                    criteria.add(Restrictions.between(parameter.getName(), parameter.getValue(), parameter.getValue2()));
                }
            }
        }

    }

    private void addAliRestrictionToCriteria() {
        if (aliFilterParams.isEmpty()) {
            return;
        }

        for (Parameter parameter : aliFilterParams) {
            addParameter(parameter, aliCr);
        }
    }

    private void addOpenstackRestrictionToCriteria() {
        if (openstackFilterParams.isEmpty()) {
            return;
        }

        for (Parameter parameter : openstackFilterParams) {
            addParameter(parameter, openstackCr);
        }
    }

    private boolean isOrderAscending() {
        return orderAscending;
    }

    public boolean isResultTransformer() {
        return resultTransformer;
    }

    public void setResultTransformer(boolean resultTransformer) {
        this.resultTransformer = resultTransformer;
    }

    private void addOrderToCriteria() {
        if (null == orderBy) {
            return;
        }

        if (isOrderAscending()) {
            openstackCr.addOrder(Order.asc(orderBy));
        } else {
            openstackCr.addOrder(Order.desc(orderBy));
        }
    }

    private void addAliOrderToCriteria() {
        if (null == orderByAli){
            return;
        }

        if (isOrderAscending()) {
            aliCr.addOrder(Order.asc(orderByAli));
        } else {
            aliCr.addOrder(Order.desc(orderByAli));
        }
    }
}
